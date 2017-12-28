//package org.usfirst.frc.team1592.robot.commands.Chassis;
//
//import org.usfirst.frc.team1592.robot.Constants;
//import org.usfirst.frc.team1592.robot.Robot;
//import org.usfirst.frc.team1592.robot.RobotMap;
//import org.usfirst.frc.team1592.robot.utils.PIDCommand1592;
//
//import com.qtech.first.qLibs.math.qMath;
//
//public class CenterOnBoilerPID extends PIDCommand1592{
//
//	//Default to 100 [in?]; Distance Limits 92 to 132
//	double setDistance_in = 100;
//	double errPI = 0;
//	double errPOld = 0;
//
//	/**
//	 * Set distance = 100 and drives to boiler
//	 */
//	public CenterOnBoilerPID() {
//		super(Constants.DrivePID.kP, Constants.DrivePID.kI, Constants.DrivePID.kD,Constants.DrivePID.period);
//        requires(Robot.chassis);
//	}
//	
//	/**
//	 * Accepts input for set distance and drives to boiler
//	 * @param setDistance
//	 */
//	public CenterOnBoilerPID(double setDistance) {
//		// Distance Limits 92 to 132 inches
//		super(Constants.DrivePID.kP, Constants.DrivePID.kI, Constants.DrivePID.kD,Constants.DrivePID.period);
//        requires(Robot.chassis);
//        setDistance = qMath.limit(setDistance, 92, 132); //in
//		this.setDistance_in = setDistance;
//	}
//
//	protected void initialize() {
//		//Set controller to target current heading and initialize
//    	Robot.chassis.setSetpoint(Robot.chassis.getAngle());
//    	Robot.chassis.enable();
//	}
//
//	protected boolean isFinished() {
//		return false;
//	}
//
//	protected void end() {
//		//Leave heading controller disabled
//		Robot.chassis.disable();
//	}
//
//	protected void interrupted() {
//		end();
//	}
//
//	@Override
//	protected double returnPIDInput() {
//		// ==== Lateral errors from the pixy  ===
//		// Distance Limits 92 to 132
//		RobotMap.boilerArduino.getBuffer();
//		//System.out.println("BOILER.Pixy: " + RobotMap.arduino.getPixy() + "LIDAR: " + RobotMap.arduino.getLeftLidar());
//		double pixyError = RobotMap.boilerArduino.getPixy();
//		double pixyErr_deg = 0;
//		if(pixyError >= 0 && pixyError <= 319) {
//			//Put on the range [-1 1]
//			pixyError= (pixyError - 160) / 319.0;
//			//atan(pixyError * tand(37.5)/160)
//			pixyErr_deg = Math.toDegrees(Math.atan(pixyError * 0.0048)); 
//		}
//		Robot.chassis.setSetpointRelative(pixyErr_deg);
//		
//		// ==== Distance error from LIDAR ====
//		if (Math.abs(pixyErr_deg) <= 2.0) {
//			double distanceError_in = RobotMap.boilerArduino.getLidar() - setDistance_in;
//			return distanceError_in/12.0;
//		} else {
//			return 0;
//		}
//	}
//
//	@Override
//	protected void usePIDOutput(double vForward) {
//		//This should be body oriented, not field oriented.
//		Robot.chassis.drive2Heading(0,vForward,false);
//	}
//
//}
