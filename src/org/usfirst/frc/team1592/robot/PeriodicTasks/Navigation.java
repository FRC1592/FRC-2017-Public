package org.usfirst.frc.team1592.robot.PeriodicTasks;

import org.usfirst.frc.team1592.arch.utils.PeriodicTask;
import org.usfirst.frc.team1592.arch.utils.Vector2D;
import org.usfirst.frc.team1592.robot.Robot;

public class Navigation extends PeriodicTask {
	private double robotX = 0;
	private double robotY = 0;
	private int dt = 20; 	//[ms]

	public Navigation() {
		super(20); 			//Default to 50Hz
	}
	
	public Navigation(int dt) {
		super(dt);
		this.dt = dt;
	}
	
	@Override
	public void run() {
		Vector2D vRobot = Robot.chassis.getVelocityField_FPS();
		//Synchronizing this block to prevent inconsistent x/y's and updates during the += operation
		synchronized (this) {
			robotX += vRobot.getX() * dt/1000d;
			robotY += vRobot.getY() * dt/1000d;
		}
	}
	
	//Skipping synchrnoized on this call; any inconsistencies during a read should be negligible
	public Vector2D getPosition_FT() {
		return new Vector2D(robotX,robotY);
	}
	
	//synchronizing setter to make sure no one else uses an inconsistent state during update
	public synchronized void setPosition(double x, double y) {
		robotX = x;
		robotY = y;
	}
	
	public synchronized void setPosition(Vector2D pos) {
		robotX = pos.getX();
		robotY = pos.getY();
	}

}
