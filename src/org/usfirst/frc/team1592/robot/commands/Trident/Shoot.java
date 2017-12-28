package org.usfirst.frc.team1592.robot.commands.Trident;

import org.usfirst.frc.team1592.robot.Constants;
import org.usfirst.frc.team1592.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class Shoot extends Command {
	boolean isSpinup;
	final double freq = 1d * 2d*Math.PI;
	final Timer timer = new Timer();
	
    public Shoot() { 
     	requires(Robot.trident);
    }

    protected void initialize() {
    	Robot.trident.setShooterRPM(Constants.Trident.SHOOTER_RPM_HIGH);
    	Robot.trident.setKickerRPM(Constants.Trident.KICKER_RPM_HIGH);
    	this.isSpinup = true;
    	//for pulsing gatherer
    	timer.start();
    }

    protected void execute() {
    	if(isSpinup) {
    		if(Robot.trident.shooterAtSpeed() && Robot.trident.kickerAtSpeed()) {
    			Robot.trident.setShooterRPM(Constants.Trident.SHOOTER_RPM);
    			Robot.trident.setKickerRPM(Constants.Trident.KICKER_RPM);
    			this.isSpinup = false;
    		}
    	}
    	
    	if(Robot.trident.shooterAtSpeed() && Robot.trident.kickerAtSpeed()) {
//        	Robot.gather.setGather(1d);
    		Robot.gather.setGather(0.8 + 0.2*Math.sin(freq*timer.get()));
        	Robot.trident.setBed(1d);  //rollers into belt
        	Robot.trident.setLift(0.9d); //belt and red lift wheels
    	} else {
        	Robot.gather.stop();
        	Robot.trident.stopBed();
        	Robot.trident.stopLift();
    	}
    }

    protected boolean isFinished() {
    	return false;
    }

    protected void end() {
    	Robot.gather.stop();
    	Robot.trident.stopBed();
    	Robot.trident.stopLift();
    }

    protected void interrupted() {
    	end();
    }
}
