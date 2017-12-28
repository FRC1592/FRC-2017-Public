package org.usfirst.frc.team1592.robot.commands.Chassis;

import org.usfirst.frc.team1592.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class SetFieldOriented extends Command {
	boolean fieldOriented;
	
    public SetFieldOriented(boolean fo) {
    	this.fieldOriented = fo;
    }

    protected void initialize() {
    	Robot.chassis.setFieldOriented(fieldOriented);
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
