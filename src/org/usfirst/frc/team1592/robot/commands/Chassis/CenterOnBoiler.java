//package org.usfirst.frc.team1592.robot.commands.Chassis;
//
//import org.usfirst.frc.team1592.robot.Robot;
//import org.usfirst.frc.team1592.robot.RobotMap;
//import org.usfirst.frc.team1592.robot.utils.PIDCommand1592;
//
//import edu.wpi.first.wpilibj.DriverStation;
//import edu.wpi.first.wpilibj.DriverStation.Alliance;
//
//public class CenterOnBoiler extends PIDCommand1592{
//
//	//Default to 100 [in?]; Distance Limits 92 to 132
//	double setDistance = 100;
//	double errPI = 0;
//	double errPOld = 0;
//	double pixyError;
//
//	/**
//	 * Set distance = 100 and drives to boiler
//	 */
//	public CenterOnBoiler() {
//		super(1.0,0.003,0.002);
//		requires(Robot.chassis);
//	}
//	
//	/**
//	 * Accepts input for set distance and drives to boiler
//	 * @param setDistance
//	 */
//	public CenterOnBoiler(double setDistance) {
//		// Distance Limits 92 to 132
//		super(1.0,0.003,0.002);
//		requires(Robot.chassis);
//		this.setDistance = setDistance;
//	}
//
//	protected void initialize() {
//		//System.out.println("CenterOnBoiler init");
//		if(DriverStation.getInstance().getAlliance() == Alliance.Red)
//			RobotMap.ledRing.setColor(false, false, true);
//		else
//			RobotMap.ledRing.setColor(true, false, false);
//		
//	}
//
//	protected boolean isFinished() {
//		return false;
//	}
//
//	protected void end() {
////		RobotMap.ledRing.setColor(false, false, false);
//	}
//
//	protected void interrupted() {
//		end();
//	}
//
//	@Override
//	protected double returnPIDInput() {
//		// Distance Limits 92 to 132
//		RobotMap.boilerArduino.getBuffer();
//		//System.out.println("BOILER.Pixy: " + RobotMap.arduino.getPixy() + "LIDAR: " + RobotMap.arduino.getLeftLidar());
//
//		// Lateral errors from the pixy  
//		pixyError = RobotMap.boilerArduino.getPixy();
//		
//		if(pixyError < 0 || pixyError > 319) {
//			pixyError = 0;
//		} else
//		{
//			//Put on the range [-1 1]
//			pixyError= (pixyError - 160) / 319.0;
//		}
//		
//		return pixyError;
//	}
//
//	@Override
//	protected void usePIDOutput(double zRate) {
//		if(pixyError < 0 || pixyError > 319) zRate = 0;
//		
//		// Distance error from lidar average, setDistance is the distance you want to stop from the peg wall
//		double distanceError = RobotMap.boilerArduino.getLidar() - setDistance;
//
//		double vForward = 0;
//		// This number will come from experiment and be the number that makes sure the LIDAR is hitting the stack
//		// Only use distance error if the pixy is actually pointed at the stack  If it is PID, the PID updates will go in here
//		if(Math.abs(pixyError) < 0.05)	vForward = distanceError * 0.02;    
//
//		//if(distanceError < 0) vForward = 0;
//
//		//Limit magnitude to 0.2*vForward
//		if(Math.abs(vForward) > 0.2)  vForward = 0.2 * Math.signum(vForward);
//
//		Robot.chassis.driveSwerve(0,vForward,-zRate,false); //  Drive!!  This should be body oriented, not field oriented.
//
//	}
//
//}
