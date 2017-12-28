package org.usfirst.frc.team1592.robot.commands.Auto;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
// TODO: Add command to master: Fake Auto in telemtry
public class FakeAutoTester extends Command {
	
	Command fakeAutoCommand;

    public FakeAutoTester() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//fakeAutoCommand = ((FakeAuto) Robot.chooser.getSelected()).getRealCommand();
    	//SmartDashboard.putData("Fake Auto mode", chooser);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
