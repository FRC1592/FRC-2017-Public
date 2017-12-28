package org.usfirst.frc.team1592.robot.commands.Chassis;

import org.usfirst.frc.team1592.arch.utils.Discontinuities;
import org.usfirst.frc.team1592.robot.Constants;
import org.usfirst.frc.team1592.robot.Robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;

public class DriveWithJoysticksPrecise extends Command {

	//Assume dt of 20ms
	//TODO: could swap over to a timer and measure dt
	private final static double dt = 0.02;		//sec
	
	//Last commands for smoothing purposes [output units/dt]
	//Set Max ramp rates (ramping disabled if set to zero)
	//A smaller ramp rate = slower changes in throttle
	//TODO: does ramp belong here or chassis? Guess it could be both...
	private static double translateRamp = 4.0 * Constants.MAX_SPEED * dt; //FPS/dt   	was 2.5
	private static double stopRamp = 1.5 * Constants.MAX_SPEED * dt; 	//FPS/dt
	private static double rotateRamp = 3.0 * Constants.MAX_TURN_RATE * dt; //deg/s/dt	was 3.0
	private double prevCmdX = 0;		//FPS
	private double prevCmdY = 0;		//FPS
	private double prevCmdZ = 0;		//FPS
	
	//======= Handling qualities =========
	private static final double kJoyDB = 0.1; //[ratio full scale]
	private static final double kCmdDB = 0.02;  //[ratio full scale]
	private static final double kJoyExpo = 3.0;

    public DriveWithJoysticksPrecise() {
    	requires(Robot.chassis);
    }
    
    public static void setRamps(double translate, double rotate, double stop) {
    	translateRamp = translate * dt;
    	rotateRamp = rotate * dt;
    	stopRamp = stop * dt;
    }

    protected void initialize() {
    	//Default to field oriented mode
    	Robot.chassis.setFieldOriented(true);
    	//Set heading controller to current position and enable
    	//Robot.chassis.setSetpoint(Robot.chassis.getAngle());
    	//Robot.chassis.enable();
    }

    protected void execute() {
    	//Get joystick commands
    	double xraw = Robot.oi.driver.getX(Hand.kLeft);
    	double xlim = Discontinuities.limit(Discontinuities.dead(xraw,kJoyDB), -1.0, 1.0);
    	double xmap = map(xlim,kJoyDB,kCmdDB);
    	//Invert Y axis to make it + when stick is pushed forward
    	double yraw = -Robot.oi.driver.getY(Hand.kLeft);
    	double ylim = Discontinuities.limit(Discontinuities.dead(yraw,kJoyDB), -1.0, 1.0);
    	double ymap = map(ylim,kJoyDB,kCmdDB);
    	//Invert Z axis to make it + when stick is pushed right?
    	double zraw = -Robot.oi.driver.getU();
    	double zlim = Discontinuities.limit(Discontinuities.dead(zraw,kJoyDB), -1.0, 1.0);
    	double zmap = map(zlim,kJoyDB,kCmdDB);
    	//Apply exponential scaling on magnitude to give better sensitivity at low inputs
    	double mag = Discontinuities.joyExpo(Math.sqrt(xmap * xmap + ymap * ymap),kJoyExpo);
    	//Convert scaled magnitude back into x/y coordinates
    	double az = Math.atan2(ymap,xmap);
    	double x = mag * Constants.MAX_SPEED * Math.cos(az) / 2d;
    	double y = mag * Constants.MAX_SPEED * Math.sin(az) / 2d;
    	//Xbox ONE z axis likes to hang out at about 0.11
    	double z = Discontinuities.joyExpo(zmap,kJoyExpo) * Constants.MAX_TURN_RATE / 2d;
    	
		//Smooth Commands
    	//TODO: Need to ramp down exponentially or on a squared function or something
    	if ((Math.abs(x) > 0) || (Math.abs(y) > 0) || stopRamp == 0) {
    		if (translateRamp > 0 ){
    			x = Discontinuities.limit(x,prevCmdX-translateRamp,prevCmdX+translateRamp);
    			y = Discontinuities.limit(y,prevCmdY-translateRamp,prevCmdY+translateRamp);
    		}
    	} else
    	{
    		//If stopping, ramp down
    		x = Math.signum(prevCmdX) * Math.max(Math.abs(prevCmdX)-stopRamp,0.0);
    		y = Math.signum(prevCmdY) * Math.max(Math.abs(prevCmdY)-stopRamp,0.0);
    	}
    	
    	//Smooth z command
    	if (Math.abs(z) > 0 || stopRamp ==0) {
    		if (rotateRamp > 0){
    			z= Discontinuities.limit(z,prevCmdZ-rotateRamp,prevCmdZ+rotateRamp);
    		}
    	} else
    	{
    		//If stopping, ramp down
    		z = Math.signum(prevCmdZ) * Math.max(Math.abs(prevCmdZ)-rotateRamp/3.0,0.0);
    	}
    	
    	//Update heading controller setpoint
//    	Robot.chassis.setSetpointRelative(z * dt);
    	
    	//Send commands to the swerve
//    	Robot.chassis.drive2Heading(x, y);
    	Robot.chassis.driveSwerve(x, y, z);
    	
    	//Capture outputs for the next set
    	prevCmdX = x;
    	prevCmdY = y;
    	prevCmdZ = z;
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.chassis.disable();
    }

    protected void interrupted() {
    	end();
    }
    
    /**
     * Maps input from magnitude [inLow,1] to [outLow,1]
     * @param in
     * @param inLow
     * @param outLow
     * @return value on output range
     */
    private static double map(double in, double inLow, double outLow) {
    	double inRange = 1-inLow;
    	double outRange = 1- outLow;
    	double in_mag = (Math.abs(in) - inLow) * outRange/inRange + outLow;
    	return in_mag * Math.signum(in);
    }
}
