package org.usfirst.frc.team1592.robot.commands.Chassis;

import org.usfirst.frc.team1592.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TurnToHeading extends Command {
	double ang;
	private final Timer timer = new Timer();
	
    public TurnToHeading(double ang) {
    	requires(Robot.chassis);
    	this.ang = ang;
    	setTimeout(2.0);
    }

    protected void initialize() {
    	Robot.chassis.setSetpoint(ang);
    	Robot.chassis.enable();
    	timer.start();
    }

    protected void execute() {
		Robot.chassis.drive2Heading(0, 0);
    }

    protected boolean isFinished() {
    	boolean done = Robot.chassis.onTarget();
        return done || isTimedOut();
    }

    protected void end() {
    	Robot.chassis.disable();
    	timer.stop();
    	timer.reset();
    }

    protected void interrupted() {
    	end();
    }
}
