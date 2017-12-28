package org.usfirst.frc.team1592.robot.commands.Chassis;

import org.usfirst.frc.team1592.arch.utils.Discontinuities;
import org.usfirst.frc.team1592.robot.Constants;
import org.usfirst.frc.team1592.robot.Robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;

public class DriveWithJoysticks_FPS_Old extends Command {

	//Assume dt of 20ms
	//TODO: could swap over to a timer and measure dt
	private final static double dt = 0.02;		//sec
	
	//Last commands for smoothing purposes [output units/dt]
	//Set Max ramp rates (ramping disabled if set to zero)
	//A smaller ramp rate = slower changes in throttle
	//TODO: does ramp belong here or chassis? Guess it could be both...
	private static double translateRamp = 2.5 * Constants.MAX_SPEED * dt; //FPS/dt   	was 2.5
	private static double stopRamp = 1.0 * Constants.MAX_SPEED * dt; 	//FPS/dt
	private static double rotateRamp = 3.0 * Constants.MAX_TURN_RATE * dt; //deg/s/dt	was 3.0
	private double prevCmdX = 0;		//FPS
	private double prevCmdY = 0;		//FPS
	private double prevCmdZ = 0;		//FPS

    public DriveWithJoysticks_FPS_Old() {
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
    	Robot.chassis.setSetpoint(0.0);
    	//TODO: should we enable heading control here?
    	//Robot.chassis.enable();
    }

    protected void execute() {
    	//Get joystick commands
    	//TODO: joystick will produce values greater than 1.0 RSS
    	double xraw = Robot.oi.driver.getX(Hand.kLeft);
    	//Invert Y axis to make it + when stick is pushed forward
    	double yraw = -Robot.oi.driver.getY(Hand.kLeft);
    	//Invert Y axis to make it + when stick is pushed forward
    	double zraw = -Robot.oi.driver.getU();
    	//Apply exponential scaling on magnitude to give better sensitivity at low inputs
    	double mag = Discontinuities.joyExpo(Math.sqrt(xraw * xraw + yraw * yraw),4);
    	//Convert scaled magnitude back into x/y coordinates
    	double az = Math.atan2(yraw,xraw);
    	double x = mag * Constants.MAX_SPEED * Math.cos(az);
    	double y = mag * Constants.MAX_SPEED * Math.sin(az);
    	//Xbox ONE z axis likes to hang out at about 0.11
    	double z = Discontinuities.joyExpo(Discontinuities.dead(zraw,0.13), 4) * Constants.MAX_TURN_RATE;
		//Smooth Commands
    	//TODO: Need to ramp down exponentially or on a squared function or something
    	if ((Math.abs(x) > Constants.DRIVE_THRESH * Constants.MAX_SPEED *1.25) || (Math.abs(y) > Constants.DRIVE_THRESH * Constants.MAX_SPEED * 1.25) || stopRamp == 0) {
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
    	//TODO: ramp turn down to stop?
    	if (Math.abs(z) > 0 || stopRamp ==0) {
    		if (rotateRamp > 0){
    			z= Discontinuities.limit(z,prevCmdZ-rotateRamp,prevCmdZ+rotateRamp);
    		}
    	} else
    	{
    		z = Math.signum(prevCmdZ) * Math.max(Math.abs(prevCmdZ)-rotateRamp/3.0,0.0);
    	}
    	//Send commands to the swervec
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
    }

    protected void interrupted() {
    	end();
    }
}
