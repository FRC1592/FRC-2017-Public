package org.usfirst.frc.team1592.robot.PeriodicTasks;

import org.usfirst.frc.team1592.arch.utils.PeriodicTask;
import org.usfirst.frc.team1592.robot.Robot;
import org.usfirst.frc.team1592.robot.RobotMap;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SDUpdater extends PeriodicTask {
	public SDUpdater() {
		//Smartdashboard only runs at 10Hz I think
		super(100); // period in ms
	}
    
	@Override
    public void run() {
		SmartDashboard.putNumber("NavX", Robot.chassis.getAngle_Deg());
    	SmartDashboard.putNumber("Flap Pos [deg]", Robot.gearFlapper.getAngleDeg());
    	SmartDashboard.putNumber("Flap Abs Enc [rev]", Robot.gearFlapper.getAbsEncAngle_Revs());
    	SmartDashboard.putBoolean("Can See Gear", Robot.gearFlapper.canSeeGear());
    	SmartDashboard.putNumber("Shooter RPM", Robot.trident.getShooterRpm());
    	SmartDashboard.putNumber("Kicker RPM", Robot.trident.getKickerRpm());

    	SmartDashboard.putNumber("LF", RobotMap.leftFrontPod.getAbsAngle());
    	SmartDashboard.putNumber("RF", RobotMap.rightFrontPod.getAbsAngle());
    	SmartDashboard.putNumber("LB", RobotMap.leftBackPod.getAbsAngle());
    	SmartDashboard.putNumber("RB", RobotMap.rightBackPod.getAbsAngle());
    	
    	SmartDashboard.putNumber("X position", Robot.nav.getPosition_FT().getX());
    	SmartDashboard.putNumber("Y position", Robot.nav.getPosition_FT().getY());
    	
    	SmartDashboard.putNumber("Z Angle", Robot.chassis.getAngle_Deg());
    	
    	SmartDashboard.putNumber("LeftFront Vel [RPM@wheel]", RobotMap.leftFrontPod.getWheelSpeed_RPM());
    	SmartDashboard.putNumber("RightFront Vel [RPM@wheel]", RobotMap.rightFrontPod.getWheelSpeed_RPM());
    	SmartDashboard.putNumber("LeftRear Vel [RPM@wheel]", RobotMap.leftBackPod.getWheelSpeed_RPM());
    	SmartDashboard.putNumber("RightRear Vel [RPM@wheel]", RobotMap.rightBackPod.getWheelSpeed_RPM());

//		RobotMap.boilerArduino.getBuffer();
//		RobotMap.gearArduino.getBuffer();
//    	System.out.println("Boiler Pixy: "+RobotMap.boilerArduino.getPixy()+", Lidar: "+RobotMap.boilerArduino.getLidar());
//    	System.out.println("Gear Pixy: "+RobotMap.gearArduino.getPixy()+", Lidar: "+RobotMap.gearArduino.getLidar());
//    	System.out.println(Robot.chassis.getSetpoint());
    }
}

