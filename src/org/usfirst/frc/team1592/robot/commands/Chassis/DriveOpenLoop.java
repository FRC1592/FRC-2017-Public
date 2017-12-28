package org.usfirst.frc.team1592.robot.commands.Chassis;

import org.usfirst.frc.team1592.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveOpenLoop extends Command {
	
	double x,y,z;
	private boolean wasFieldOriented;

    public DriveOpenLoop(double x, double y, double z) {
        // Use requires() here to declare subsystem dependencies
    	requires(Robot.chassis);
    	this.x=x;
    	this.y=y;
    	this.z=z;
    }

    public DriveOpenLoop(double ang, double mag) {
    	this(mag * Math.cos(Math.toRadians(ang)), mag * Math.sin(Math.toRadians(ang)), 0);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	wasFieldOriented = Robot.chassis.isFieldOriented();
    	Robot.chassis.setFieldOriented(false);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		Robot.chassis.driveSwerve(x, y, z);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	//Return chassis field oriented to previous state just in case next commands assumes
    	Robot.chassis.setFieldOriented(wasFieldOriented);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
