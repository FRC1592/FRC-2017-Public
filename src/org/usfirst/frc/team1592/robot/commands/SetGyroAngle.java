package org.usfirst.frc.team1592.robot.commands;

import org.usfirst.frc.team1592.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class SetGyroAngle extends Command {
	
	private double ang;
	
    public SetGyroAngle(double ang) {
    	this.ang = ang;
    }

    protected void initialize() {
    	//(re)set angle and position
    	Robot.chassis.setRobotAngle(ang);    	
    	//Reset nav
    	Robot.nav.setPosition(0.0,0.0);
    	//Update chassis command to prevent sudden movement (if active)
    	Robot.chassis.setSetpoint(Robot.chassis.getAngle_Deg());
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
