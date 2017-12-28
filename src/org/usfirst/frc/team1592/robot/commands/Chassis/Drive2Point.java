/**
 * Coordinate Frame:
 * 	x = Forward
 *  y = Right
 *  z = Up
 */

package org.usfirst.frc.team1592.robot.commands.Chassis;

import org.usfirst.frc.team1592.arch.motionProfile.TrajectoryPoint;
import org.usfirst.frc.team1592.arch.motionProfile.TrapezoidProfile;
import org.usfirst.frc.team1592.arch.utils.PIDCommand1592;
import org.usfirst.frc.team1592.arch.utils.PIDController1592;
import org.usfirst.frc.team1592.arch.utils.Vector2D;
import org.usfirst.frc.team1592.robot.Constants;
import org.usfirst.frc.team1592.robot.Robot;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.PIDCommand;

/**
 *
 */
public class Drive2Point extends PIDCommand1592 {

	//Profile object
	private TrapezoidProfile profile;
	//Timer to pick points along the profile
	private final Timer timer = new Timer();
	//Intermediate points along the profile
	private TrajectoryPoint pnt;
	//Threshold for checking when commanded position = target
	private final double kOnTargetThresh = 3.0/12.0; //3 inches [ft]
	//Desired position
	private Vector2D target;
	//Azimuth of linear move
	private double azimuth; //radians
	//Total distance of driving
	private double totalDistance;
	//Init Position
	private Vector2D initPos;
	//Error to target
	private Vector2D errorTarget;
	//Timeout once profile is finished
	private final double kTimeOut = 2d;
	//Default max velocity and acceleration
	private double vMax = 8; //[ft/s]
	private double aMax = 4.0; //[ft/s/s]
	//Flag to specify if drive should interpret inputs relative to current position
	private boolean rel = false;
	//X PID controller
	private final PIDController1592 ctrlX = getPIDController();
	private final PIDController1592 ctrlY;
	//Need output properties to coordinate controllers
	private double xOutput = 0;
	private double yOutput = 0;
	//Used to return chassis to previous state when finished with it
	private boolean wasFieldOriented;
	
	
	/** A source which calls {@link PIDCommand#returnPIDInput()} */
	private PIDSource source = new PIDSource()
	{
		public void setPIDSourceType(PIDSourceType pidSource)
		{}

		public PIDSourceType getPIDSourceType()
		{
			return PIDSourceType.kDisplacement;
		}

		public double pidGet()
		{
			return returnPIDInputY();
		}
	};
	
	/** An output which calls {@link PIDCommand#usePIDOutput(double)} */
	private PIDOutput output = new PIDOutput()
	{

		public void pidWrite(double output)
		{
			usePIDOutputY(output);
		}
	};

	/**
	 * Constructor
	 * @param target_ft - <Vector> coordinates of the target [ft]
	 * @param vMax - max velocity of motion profile [FPS]
	 * @param aMax - max accel of motion profile [ft/s/s]
	 */
    public Drive2Point(Vector2D target_ft, double vMax, double aMax) {
    	super(Constants.DrivePID.kP, Constants.DrivePID.kI, Constants.DrivePID.kD,Constants.DrivePID.period);
        requires(Robot.chassis);
		this.target = target_ft;
    	this.vMax = vMax;
    	this.aMax = aMax;
    	ctrlX.setIZone(Constants.DrivePID.iZone);
    	ctrlY = new PIDController1592(Constants.DrivePID, source, output);
    }
    
    /**
     * Constructor using default max velocity and accel of the motion profile
     * @param target_ft - <Vector> coordinates of the target [ft]
     */
    public Drive2Point(Vector2D target_ft) {
    	super(Constants.DrivePID.kP, Constants.DrivePID.kI, Constants.DrivePID.kD,Constants.DrivePID.period);
    	requires(Robot.chassis);
    	this.target = target_ft;
    	ctrlX.setIZone(Constants.DrivePID.iZone);
    	ctrlY = new PIDController1592(Constants.DrivePID, source, output);
    	}
    
    /**
     * Constructor using default max velocity and accel of the motion profile
     * Accepts inputs in polar form
     * @param ang - <Double> angle to drive at
     * @param mag - <Double> distance to drive [ft]
     */
    public Drive2Point(double ang, double mag) {
		this(new Vector2D(mag * Math.cos(Math.toRadians(ang)), mag * Math.sin(Math.toRadians(ang))));
    }
    
    /**
     * Constructor using default max velocity and accel of the motion profile
     * Inputs are in polar form
     * @param ang - <Double> angle to drive at
     * @param mag - <Double> distance to drive [ft]
	 * @param vMax - max velocity of motion profile [FPS]
	 * @param aMax - max accel of motion profile [ft/s/s]
     */
    public Drive2Point(double ang, double mag, double vMax, double aMax) {
    	this(ang, mag);
    	this.vMax = vMax;
    	this.aMax = aMax;
    }
    
    /**
     * Constructor using default max velocity and accel of the motion profile
     * @param ang - <Double> angle to drive at
     * @param mag - <Double> distance to drive [ft]
     */
    public Drive2Point(double ang, double mag, boolean rel) {
		this(ang, mag);
		this.rel = rel;
    }
    
    /**
     * Constructor using default max velocity and accel of the motion profile
     * @param ang - <Double> angle to drive at
     * @param mag - <Double> distance to drive [ft]
	 * @param vMax - max velocity of motion profile [FPS]
	 * @param aMax - max accel of motion profile [ft/s/s]
     */
    public Drive2Point(double ang, double mag, double vMax, double aMax, boolean rel) {
    	this(ang, mag);
    	this.vMax = vMax;
    	this.aMax = aMax;
    	this.rel = rel;
    }
    

    // Called just before this Command runs the first time
    protected void initialize() {
//    	RobotMap.dataLogger.outputEvent("Starting Drive2Point");
    	//Enable PID Heading controller
//    	Robot.chassis.setSetpoint(180); //TODO: temp; should be set elsewhere
    	Robot.chassis.enable();
    	initPos = Robot.nav.getPosition_FT();
    	//If commanding in relative mode, add start position to vector
    	if(rel)	target = target.add(initPos);
    	//Compute error
    	errorTarget = target.sub(initPos);
    	//Compute total length and direction of the movement
        totalDistance = errorTarget.getMagnitude();
        azimuth = errorTarget.getAngleRad();
        //TODO: need to get initial velocity or assume starting at rest?
        profile = new TrapezoidProfile(totalDistance,0.0,vMax,aMax); //Units are going to be feet
        //initialize pnt
    	pnt = profile.getTrajPoint(0.0);
    	//Timeout 2 seconds after finishing profile in case stuck
    	setTimeout(profile.getTrajEndTime() + kTimeOut);
    	//Start tracking time
		timer.start();		
		//Start controllers
		ctrlX.enable();
		ctrlY.enable();
    	wasFieldOriented = Robot.chassis.isFieldOriented();
    	//Put chassis in field oriented mode
    	Robot.chassis.setFieldOriented(true);
		System.out.println("Starting Drive 2 Point");
    }
    
	@Override
	//Update setpoint and return actual position
	protected double returnPIDInput() {
		// get next distance along the trajectory
    	pnt = profile.getTrajPoint(timer.get());
    	double posMag = pnt.getPosition();
    	//Update X setpoint
    	double xSP = posMag* Math.cos(azimuth)+initPos.getX();
    	ctrlX.setSetpoint(xSP);
    	//Compute error to target for finish condition
    	double posNow = Robot.nav.getPosition_FT().getX();
    	errorTarget.setX(target.getX() - posNow);
    	//Return current pos
		return posNow;
	}

	//Update setpoint and return actual position
	protected double returnPIDInputY() {
		// get next distance along the trajectory
    	pnt = profile.getTrajPoint(timer.get());
    	double posMag = pnt.getPosition();
    	//Update Y setpoint
    	double ySP = posMag* Math.sin(azimuth)+initPos.getY();
    	ctrlY.setSetpoint(ySP);
    	//Compute error to target for finish condition
    	double posNow = Robot.nav.getPosition_FT().getY();
    	errorTarget.setY(target.getY() - posNow);
    	//Return current point
		return posNow;
	}
    
    
    @Override
    // Update drive output
	protected void usePIDOutput(double output) {
    	xOutput = output;
	}
    
    
    // Update drive output
	protected void usePIDOutputY(double output) {
    	yOutput = output;
    }
    
    // Use execute to pass the drive command
    protected void execute() {
    	//Send the drive command
    	double vx = pnt.getVelocity() * Math.cos(azimuth);
    	double vy = pnt.getVelocity() * Math.sin(azimuth);
    	Robot.chassis.drive2Heading(xOutput+vx, yOutput+vy);
    }
  

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	//Are x and y error within bounds
    	Boolean onTarget = (Math.abs(errorTarget.getX()) <= kOnTargetThresh) && (Math.abs(errorTarget.getY()) <= kOnTargetThresh);
    	//Log exit reason
    	if (isTimedOut()) {
//    		RobotMap.dataLogger.outputEvent("Finished Driving on timeout");
    		System.out.println("Finished Driving on timeout");
    		}
    	else if (onTarget){
//    		RobotMap.dataLogger.outputEvent("Finished Driving on target");
    		System.out.println("Finished Driving on target");
    		}
    	//Stop if you get close enough (or time-out)
		return isTimedOut() || onTarget;
    }

    // Called once after isFinished returns true
    protected void end() {
    	//Stop the drive
    	Robot.chassis.disable();
    	Robot.chassis.driveSwerve(0.0, 0.0, 0.0);
    	//Return chassis field oriented to previous state just in case next commands assumes
    	Robot.chassis.setFieldOriented(wasFieldOriented);
    	//Stop computing error
    	ctrlX.disable();
    	ctrlY.disable();
    	//I don't think these calls are necessary
    	timer.stop();
    	timer.reset();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }



}
