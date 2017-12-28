package org.usfirst.frc.team1592.robot.commands;

import org.usfirst.frc.team1592.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class SetChassisFeedFwdOnly extends Command {
	
    public SetChassisFeedFwdOnly() {
    }

    protected void initialize() {
    	//Choose feed-forward slot
    	System.out.println("Jumped to feed-forward only");
    	Robot.chassis.setVelPIDFDrive(1);
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {
    }

    protected void interrupted() {
    	end();
    }
}
