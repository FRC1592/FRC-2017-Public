package org.usfirst.frc.team1592.robot.commands.Ballarator;

import org.usfirst.frc.team1592.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Gather extends Command {

    public Gather() {
    	requires(Robot.gather);
    }

    protected void initialize() {
    }

    protected void execute() {
    	Robot.gather.gather();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.gather.stop();
    }

    protected void interrupted() {
    	end();
    }
}
