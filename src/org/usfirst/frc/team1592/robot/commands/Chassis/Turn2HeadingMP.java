package org.usfirst.frc.team1592.robot.commands.Chassis;

import org.usfirst.frc.team1592.arch.motionProfile.TrajectoryPoint;
import org.usfirst.frc.team1592.arch.motionProfile.TrapezoidProfile;
import org.usfirst.frc.team1592.arch.utils.Discontinuities;
import org.usfirst.frc.team1592.arch.utils.PIDCommand1592;
import org.usfirst.frc.team1592.arch.utils.PIDConstants;
import org.usfirst.frc.team1592.arch.utils.PIDController1592;
import org.usfirst.frc.team1592.robot.Constants;
import org.usfirst.frc.team1592.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public class Turn2HeadingMP extends PIDCommand1592 {
	private TrapezoidProfile profile;
	//Timer to pick points along the profile
	private final Timer timer = new Timer();
	//Intermediate points along the profile
	private TrajectoryPoint pnt;
	//Desired position
	private double _target;
	//Initial Position
	private double _initPos;
	//Error to target
	private double _errorTarget;
	//Timeout once profile is finished
	private double kTimeOut = 2d;
	//Default max velocity and acceleration
	//TODO: what are good numbers?
	private double _vMax = 360; //[deg/s]
	private double _aMax = 180; //[deg/s/s]
	//PID Controller underlying this class
	private final PIDController1592 controller = getPIDController();
	
	private final double iZone = 0*Constants.MAX_TURN_RATE;
	
	/**
	 * Constructor using default max vel and accel and timeout, period, and zero gains
	 * @param target desired angle in degrees
	 */
    public Turn2HeadingMP(double target) {
    	//TODO: should these be zero to be pure feed-forward?
    	//TODO: should it include kD?
    	super(Constants.TurnPID.kP, Constants.TurnPID.kI, 0.0);
    	requires(Robot.chassis);
    	this._target = target;
    }
    
	/**
	 * Constructor using default max vel and accel and timeout, period
	 * @param target desired angle in degrees
	 * @param <PIDConstants> gains
	 */
    public Turn2HeadingMP(double target,PIDConstants K) {
    	super(K.kP,K.kI,K.kD);
    	requires(Robot.chassis);
    	this._target = target;
    	controller.setIZone(iZone);
    }
    
	/**
	 * Constructor using default max vel and accel
	 * @param target desired angle in degrees
	 * @param <PIDConstants> gains
	 * @param time after profile is over to stop [s]
	 */
    public Turn2HeadingMP(double target,PIDConstants K,double timeout) {
    	super(K.kP,K.kI,K.kD);
    	requires(Robot.chassis);
    	this._target = target;
    	this.kTimeOut = timeout;
    	controller.setIZone(iZone);
    }
    
	/**
	 * Constructor
	 * @param target desired angle in degrees
	 * @param <PIDConstants> gains
	 * @param maximum turn rate [deg/s]
	 * @param maxumum ang accel [deg/s/s]
	 */
    public Turn2HeadingMP(double target,PIDConstants K,double vmax,double amax) {
    	super(K.kP,K.kI,K.kD);
    	requires(Robot.chassis);
    	this._target = target;
    	this._vMax = vmax;
    	this._aMax = amax;
    	controller.setIZone(iZone);
    }
    
	/**
	 * Constructor
	 * @param target desired angle in degrees
	 * @param <PIDConstants> gains
	 * @param maximum turn rate [deg/s]
	 * @param maxumum ang accel [deg/s/s]
	 * @param time after profile is over to stop [s]
	 */
    public Turn2HeadingMP(double target,PIDConstants K,double vmax,double amax,double timeout) {
    	super(K.kP,K.kI,K.kD);
    	requires(Robot.chassis);
    	this._target = target;
    	this._vMax = vmax;
    	this._aMax = amax;
    	this.kTimeOut = timeout;
    	controller.setIZone(iZone);
    }

    protected void initialize() {
//    	RobotMap.dataLogger.outputEvent("Starting Turn2Heading");
    	//Get current position
    	_initPos = Robot.chassis.getAngle_Deg() % 360d;
    	//Wrap on [0,360)
    	if (_initPos < 0) {_initPos += 360d;}
    	//Compute continuous error to target
    	_errorTarget = calcTargetError(_target,_initPos);
    	//Compute profile
    	profile = new TrapezoidProfile(_errorTarget,Robot.chassis.getRate_DPS(),_vMax,_aMax);
    	//initialize pnt
    	pnt = profile.getTrajPoint(0.0);
    	//Timeout 2 seconds after finishing profile in case stuck
    	setTimeout(profile.getTrajEndTime() + kTimeOut);
    	//Initialize chassis controller
    	controller.setSetpoint(_initPos);
    	//Set controller to work on [0,360)
    	controller.setContinuous(true);
    	controller.setInputRange(0.0,360.0);
    	controller.setAbsoluteTolerance(1.0); //[deg]
    	//TODO: these should probably be a LOT lower since we're using a feed forward scheme
    	controller.setOutputRange(-0.05*Constants.MAX_TURN_RATE, 0.05*Constants.MAX_TURN_RATE);
    	//Turn on chassis
    	controller.enable();
    	//Start tracking time
    	timer.start();
    }
    

	@Override
	protected double returnPIDInput() {
    	//Extract trajectory point
    	pnt = profile.getTrajPoint(timer.get());
    	//Set chassis to the next point
    	double sp = pnt.getPosition() + _initPos;
    	if (sp < 0) sp += 360.0;
    	else if (sp > 360) sp -= 360d;
    	controller.setSetpoint(sp);
    	//Get current angle
    	double ang = Robot.chassis.getAngle_Deg();
    	//Update target error
    	_errorTarget = calcTargetError(_target,ang);
		return ang;
	}

	@Override
	protected void usePIDOutput(double output) {
		//Rate cmd is commanded velocity (feed fwd) + output correction
		double zRateCmd = output + Math.toRadians(pnt.getVelocity()); //[rad/s]
		Robot.chassis.driveSwerve(0.0, 0.0, zRateCmd);
	}
	
	/**
	 * Returns error on the domain (-180,180]
	 * @param target
	 * @param position
	 * @return error
	 */
	private double calcTargetError(double target, double position) { 
		double error = (target - position) % 360d;
    	if(Math.abs(error) > 180)
		{
			if (error > 0){ error -= 360;}
			else {error += 360;}
		}
    	return error;
	}

    protected boolean isFinished() {
    	//Log exit method
    	boolean isOnTarget = Math.abs(_errorTarget) <= 1.0;
    	if (isTimedOut()) {
//    		RobotMap.dataLogger.outputEvent("Finished Turning on timeout");
    		System.out.println("Finished turning on timeout");
    	}
    	else if (isOnTarget) {
//    		RobotMap.dataLogger.outputEvent("Finished Turning on target");
    		System.out.println("Finished turning on target");
    	}
        return isOnTarget || isTimedOut();
    }

    protected void end() {
    	//Stop chassis
    	Robot.chassis.driveSwerve(0.0, 0.0, 0.0);
    	System.out.println(_target);
    	Robot.chassis.setSetpoint(Discontinuities.wrapAngle0To360Deg(_target)); //0-360
    	//Stop computing error
    	controller.disable();
    	//I don't think these calls are necessary
    	timer.stop();
    	timer.reset();
    }

    protected void interrupted() {
    	end();
    }

}
