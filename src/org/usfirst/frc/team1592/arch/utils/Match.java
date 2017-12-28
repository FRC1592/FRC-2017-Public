package org.usfirst.frc.team1592.arch.utils;

import edu.wpi.first.wpilibj.Timer;

public class Match {
	Timer autoTimer = new Timer();
	Timer teleopTimer = new Timer();
	Timer matchTimer = new Timer();
	
	boolean auto, teleop;
	
	public Match() {
	}
	
	public void autoInit() {
		auto = true;
		teleop = false;
		autoTimer.start();
		matchTimer.start();
	}
	
	public void teleopInit() {
		auto = false;
		teleop = true;
		autoTimer.stop();
		teleopTimer.start();
		if(matchTimer.get() == 0) //Start match timer even if we did not go into auto mode
			matchTimer.start();
	}
	
	public void disabledInit() {
		auto = false;
		teleop = false;
		autoTimer.stop();
		teleopTimer.stop();
		matchTimer.stop();
	}
	
	public double getAutoTime() {
		return autoTimer.get();
	}
	
	public double getTeleopTime() {
		return teleopTimer.get();
	}
	
	public double getMatchTime() {
		return matchTimer.get();
	}
	
	public boolean isAuto() {
		return auto;
	}
	
	public boolean isTeleop() {
		return teleop;
	}
}
