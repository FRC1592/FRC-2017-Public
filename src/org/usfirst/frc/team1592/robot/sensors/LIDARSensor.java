//package org.usfirst.frc.team1592.robot.sensors;
//
//import org.usfirst.frc.team1592.robot.RobotMap;
//
//import edu.wpi.first.wpilibj.SensorBase;
//
//
//public class LIDARSensor extends SensorBase{
//	        
//	Boolean isLeft = true;
//	
//    public LIDARSensor(Boolean IsLeft) {  
//    	if (IsLeft == true) isLeft = true;
//    	initLidar();     	          	  
//    }
//    
//    public LIDARSensor() {
//    	isLeft = true;
//    	initLidar();
//    }
//   
//	public void initLidar() {
//        // nothing to do
//	}
//
//	public int getDistance() {
//		/*
//		if (isLeft) return RobotMap.arduinoBoilerSensor.getLeftLidar();
//		return RobotMap.arduinoBoilerSensor.getRightLidar();
//		*/
//		return RobotMap.boilerArduino.getLidar();
//	}
//	
//	public void initDefaultCommand() {
//        // nothing to do
//    }
//	
//	
//}
//
