package org.usfirst.frc.team1592.robot.commands.Trident;

import org.usfirst.frc.team1592.robot.Robot;
import org.usfirst.frc.team1592.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class StopClimber extends Command {
    public StopClimber() {
    	requires(Robot.trident);
    }

    protected void initialize() {
    	Robot.trident.setShooterRPM(0d);
    	RobotMap.tridentMaster.disable();
    }

    protected void execute() {
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
