package org.usfirst.frc.team1592.robot.commands.Chassis;

import org.usfirst.frc.team1592.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ToggleFieldOriented extends Command {
    public ToggleFieldOriented() {
    }

    protected void initialize() {
    	Robot.chassis.setFieldOriented(!Robot.chassis.isFieldOriented());
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
