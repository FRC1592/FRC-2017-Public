/**
 * 
 */
package org.usfirst.frc.team1592.arch.motionProfile;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.PIDCommand;

/**
 * @author ddyer
 *
 */
public class ExampleFollowProfileClocked extends PIDCommand {
	private TrapezoidProfile profile;
	//Timer to pick points along the profile
	private Timer time = new Timer();
	private TrajectoryPoint pnt;
	//Threshold for checking when commanded position = target
	private final double threshold = 0.01;
	//Desired position
	private double target;

	public ExampleFollowProfileClocked(double target, double period) {
		super(0.0, 0.0, 0.0, period);
		// Use requires() here to declare subsystem dependencies
		//requires(Robot.exampleSubsystem);
		//TrapezoidProfile(targetPosition,initialVelocity,maxVelocityMagnitude,accelerationMagnitude)
		this.target = target;
	}
	
	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		//start the timer used to interpolate the motion profile
		profile = new TrapezoidProfile(target,2,1,1.5);
		time.start();
	}

	/* (non-Javadoc)
	 * @see edu.wpi.first.wpilibj.command.PIDCommand#returnPIDInput()
	 */
	@Override
	protected double returnPIDInput() {
		// Not actually doing PID, so no need to return any sensor input
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.first.wpilibj.command.PIDCommand#usePIDOutput(double)
	 */
	@Override
	protected void usePIDOutput(double output) {
		//Believe this gets called at the specified period, so this is a good
		//place to calculate the profile point
		//get point along profile
		pnt = profile.getTrajPoint(time.get());
		//TODO: send pnt.getPosition() to command like driveToDistance
	}

	/* (non-Javadoc)
	 * @see edu.wpi.first.wpilibj.command.Command#isFinished()
	 */
	@Override
	protected boolean isFinished() {
		//Finish when commanded position reaches target value.
		double error = pnt.getPosition() - target;
		return Math.abs(error) <= threshold;
	}

}
