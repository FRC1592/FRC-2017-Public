//package org.usfirst.frc.team1592.robot.commands.Chassis;
//
//import org.usfirst.frc.team1592.robot.Robot;
//import org.usfirst.frc.team1592.robot.RobotMap;
//
//import com.qtech.first.qLibs.math.Vector;
//
//import edu.wpi.first.wpilibj.command.Command;
//
//public class CenterOnGearEncoders extends Command{
//	double errPIG = 0;
//	double errPDG = 0;
//	double errPOldG = 0; 
//	Vector _target;
//	double distanceErrorG = 0;
//
//	public CenterOnGearEncoders(Vector target) {
//		requires(Robot.chassis);
//		_target = target;
//	}
//
//	protected void initialize() {
//		//System.out.println("CenterOnBoiler init");
//	}
//
//	protected void execute() {
//		double setDistance = 9;
//		RobotMap.gearArduino.getBuffer();
////		System.out.println("GEAR....Pixy: " + RobotMap.arduinoGear.getPixy() + "LIDAR: " + RobotMap.arduinoGear.getLeftLidar());
//
//		// Distance error from lidar average, setDistance is the distance you want to stop from the peg wall
//		Vector error = _target.sub(Robot.nav.getPosition_FT());
//		distanceErrorG = error.getMagnitude() * 12d - setDistance;
//
//		// Lateral errors from the pixy  
//		double pixyErrorG = RobotMap.gearArduino.getPixy();
//		double slewRate = 0;
//		if (pixyErrorG >= 0 && pixyErrorG <= 319)
//		{
//			pixyErrorG= ((pixyErrorG - 175)/319.0);
//	        
//			System.out.println("Lidar: " + distanceErrorG + " Pixy: " + RobotMap.gearArduino.getPixy() + "Err: " + pixyErrorG);
//	
//			//Integral Term
//			errPIG += pixyErrorG;
//			//Derivative Term
//			errPDG = errPOldG - pixyErrorG;
//			errPOldG = pixyErrorG;
//			//Compute PID command
//			slewRate= pixyErrorG*1.8 + errPIG*0.025 + errPDG*0.001;    // Only use distance error if the pixy is actually pointed at the stack  If it is PID, the PID updates will go in here
//		} else {
//			slewRate = 0;
//			System.out.println("Pixy out of range: "+pixyErrorG);
//		}
//
//		//headingError=gearAngle-getAngle();
//		//zRateG=zRateCmd;
//		double vForwardG = 0;
//		// This number will come from experiment and be the number that makes sure the LIDAR is hitting the stack
//		// Only use distance error if the pixy is actually pointed at the stack  If it is PID, the PID updates will go in here
//		if(Math.abs(pixyErrorG)<0.1) vForwardG = distanceErrorG * 0.01;    
//		
//		//Limit the output
//		if(Math.abs(vForwardG) > 0.3) vForwardG = 0.3 * Math.signum(vForwardG);
//		
//		//TODO: shouldn't this be drive2Heading?
//		Robot.chassis.driveSwerve(-slewRate, -vForwardG,0,false); //  Drive!!  This should be body oriented, not field oriented.
//	}
//
//	protected boolean isFinished() {
////		return false;
//		return Math.abs(distanceErrorG) < 2d/12d; //[ft]
//	}
//
//	protected void end() {
//	}
//
//	protected void interrupted() {
//		end();
//	}
//
//}
