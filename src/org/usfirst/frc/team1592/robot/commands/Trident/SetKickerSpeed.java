package org.usfirst.frc.team1592.robot.commands.Trident;

import org.usfirst.frc.team1592.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SetKickerSpeed extends Command {
	double rpm;
	
    public SetKickerSpeed(double rpm) {
    	requires(Robot.trident);
    	this.rpm = rpm;
    }

    protected void initialize() {
    	Robot.trident.setKickerRPM(rpm);
    }

    protected void execute() {
    	System.out.println("Kicker loading: "+Math.floor((Robot.trident.getKickerRpm()/rpm)*100)+"%");
    }

    protected boolean isFinished() {
        return Robot.trident.kickerAtSpeed();
    }

    protected void end() {
    }

    protected void interrupted() {
    	Robot.trident.setKickerRPM(0);
    	end();
    }
}
