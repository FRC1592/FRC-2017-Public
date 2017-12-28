package org.usfirst.frc.team1592.arch.motionProfile;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ExampleFollowProfile extends Command {
	private TrapezoidProfile profile;
	//Timer to pick points along the profile
	private Timer time = new Timer();
	private TrajectoryPoint pnt;
	//Threshold for checking when commanded position = target
	private final double threshold = 0.01;
	//Desired position
	private double target;
	
	//TODO: expand/add constructors to accept whatever inputs are desired
	public ExampleFollowProfile(double target) {
		// Use requires() here to declare subsystem dependencies
		//requires(Robot.exampleSubsystem);
		//TrapezoidProfile(targetPosition,initialVelocity,maxVelocityMagnitude,accelerationMagnitude)
		profile = new TrapezoidProfile(target,2,1,1.5);
		this.target = target;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		time.start();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		//get point along profile
		pnt = profile.getTrajPoint(time.get());
		//TODO: send pnt.getPosition() to command like driveToDistance
		
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		//Finish when commanded position reaches target value.
		double error = pnt.getPosition() - target;
		return Math.abs(error) <= threshold;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
