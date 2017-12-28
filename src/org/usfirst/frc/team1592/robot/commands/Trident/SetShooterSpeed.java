package org.usfirst.frc.team1592.robot.commands.Trident;

import org.usfirst.frc.team1592.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SetShooterSpeed extends Command {
	double rpm;
	
    public SetShooterSpeed(double rpm) {
    	requires(Robot.trident);
    	this.rpm = rpm;
    }

    protected void initialize() {
    	Robot.trident.setShooterRPM(rpm);
    }

    protected void execute() {
    	System.out.println("Shooter loading: "+Math.floor((Robot.trident.getShooterRpm()/rpm)*100)+"%");
    }

    protected boolean isFinished() {
        return Robot.trident.shooterAtSpeed();
    }

    protected void end() {
    }

    protected void interrupted() {
    	Robot.trident.setShooterRPM(0);
    	end();
    }
}
