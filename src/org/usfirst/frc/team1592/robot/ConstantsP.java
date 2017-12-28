/**
 * This file should define all robot-specific parameters or parameters that need tuning.
 * You should NOT bury constants or "magic numbers" throughout the code. If a constant
 * is in the code somewhere else, it better really be a constant.
 */
package org.usfirst.frc.team1592.robot;

import org.usfirst.frc.team1592.arch.utils.CANTalonPID;
import org.usfirst.frc.team1592.arch.utils.PIDConstants;
import org.usfirst.frc.team1592.arch.utils.Vector2D;

/**
 * All constants should reflect the configuration of the practice robot.
 */
public final class ConstantsP {
	//NOTE: if NOT outputting in velocity mode, normalize width and length by radius 
	public static final double SWERVE_WIDTH = 22.5d / 12d; //Front of robot [ft]
	public static final double SWERVE_LENGTH = 20.75d / 12d; //Side of robot [ft]
	public static final double SWERVE_RADIUS = Math.sqrt(SWERVE_WIDTH*SWERVE_WIDTH + SWERVE_LENGTH*SWERVE_LENGTH) / 2d; //[ft]
	public static final double WHEEL_RAD = 3.25 / 2d / 12d; //[ft]
	public static final double ENC2AXLE_RATIO = 24.0/64.0/1.6; //reduction from motor to encoder is 5:1.  Total reduction from motor to wheel is 21.3333
	//TODO: need to maximize without going much over capability
	public static final double MAX_SPEED = 10.0;	//[FPS]
	//Note: max turn rate for chassis would be MAX_SPEED / SWERVE_RADIUS ~= 10FPS/1.275FT ~= 450 deg/s
	public static final double MAX_TURN_RATE = 1.0 * MAX_SPEED / SWERVE_RADIUS; //[rad/sec]
	//Minimum input speed to consider driving
	public static final double DRIVE_THRESH = 0.04*1.1; //[]

	//Swerve PIDs
	public static final CANTalonPID driveWheelPID = new CANTalonPID(0.1,0.0002,0.09,0.04,6000);
	public static final CANTalonPID rotatePodPID = new CANTalonPID(4.0,0.004,0.0);
	
	public static final boolean isVelDrive = true;


	/*
	 * To calc Kf:
	 * 1. run robot at full speed (really needs to be under load - i.e. running around)
	 * 2. record max RPM@encoder for each wheel
	 * 3. Kf = 149.85/RPM_MAX (see TalonSRX Software Ref Manual for derivation)
	 */
	
	/**
	 * Helper method to convert encoder speed to a feed-forward gain
	 * @param RPM_enc: RPM measured at encoder
	 * @return Kf: TalonSRX feed-forward gain
	 */
	private static final double RPM_enc2Kf(double RPM_enc) {
		double RPM2COUNTS_PER_100MS = 4096.0/10.0/60.0;
		double maxOut = 1023.0;
		double Kf = maxOut / (RPM_enc * RPM2COUNTS_PER_100MS);
		return Kf;
	}
	
	/**
	 * Helper method to convert wheel speed to a feed-forward gain
	 * @param RPM_wheel: RPM measured at wheel
	 * @return Kf: TalonSRX feed-forward gain
	 */
	private static final double RpmWheel2Kf(final double RPM_wheel) {
		return RPM_enc2Kf(RPM_wheel / ENC2AXLE_RATIO);
	}
	
	public final static class LFSwerve {
		public static final int DRIVE_CHAN = 0;  //Flight 15, Practice 0
		public static final int ROTATE_CHAN = 1; //Flight 4, Practice 1

		public static final double OFFSET = 0.059+0.5; //F: 0.133, P: 0.910
		public static final double Kf = RPM_enc2Kf(2845.8); //[talon_output/(counts/100ms)] TODO: derive for each wheel

		public static final Vector2D LOC = new Vector2D(SWERVE_LENGTH/2, SWERVE_WIDTH/2);
	}
	
	public final static class RFSwerve {
		public static final int DRIVE_CHAN = 2; //F: 0, P: 2
		public static final int ROTATE_CHAN = 3; //F: 11, P: 3

		public static final double OFFSET = 0.361+0.5; //F:0.936, P: 0.715 (was 0.763)
		public static final double Kf = RPM_enc2Kf(3067.4); // [talon_output/(counts/100ms)] TODO: derive for each wheel

		public static final Vector2D LOC = new Vector2D(SWERVE_LENGTH/2, -SWERVE_WIDTH/2);
	}

	public final static class LBSwerve {
		public static final int DRIVE_CHAN = 4; //F: 14, P: 4
		public static final int ROTATE_CHAN = 5; //F: 5, P:5

		public static final double OFFSET = 0.0+0.5; //F:0.926 P:0.037
		public static final double Kf = RPM_enc2Kf(2866.6); //[talon_output/(counts/100ms)] TODO: derive for each wheel

		public static final Vector2D LOC = new Vector2D(-SWERVE_LENGTH/2, SWERVE_WIDTH/2);
	}

	public final static class RBSwerve {
		public static final int DRIVE_CHAN = 6; //F:1, P: 6
		public static final int ROTATE_CHAN = 7; //F: 10, P:7

		public static final double OFFSET = 0.809+0.5; //F:0.358, P: 0.690
		public static final double Kf = RPM_enc2Kf(2965.4); //[talon_output/(counts/100ms)] TODO: derive for each wheel

		public static final Vector2D LOC = new Vector2D(-SWERVE_LENGTH/2, -SWERVE_WIDTH/2);
	}

	//NOTE: kP was 0.012[rad/sec / deg = 180/(pi*sec)]
	public static final PIDConstants TurnPID = new PIDConstants(Math.toRadians(2.5),0.00015,0.05,0d,0.02);
	
	public static final PIDConstants DrivePID = new PIDConstants(1d,0d,0d,0d,0.02); // error in [FPS/ft]

	public final class LEDs {
		public static final int RED_CHAN = 1;
		public static final int GRN_CHAN = 2;
		public static final int BLU_CHAN = 3;
		public static final int BAUD = 9600;
	}
	
	public final static class Trident {
		public static final int MASTER_CHAN = 8; //F:2, P: 8
		public static final int SLAVE_CHAN = 9; //F: 3, P:9
		public static final int KICKER_CHAN = 10; //F: 9, P:10
		public static final int LIFT_CHAN = 11; //F: 8, P:11
		public static final int BED_CHAN = 13; //F:13, P:13
		
		//Orlando:
//		public static final double shooterF = 0.027d;
//		public static final double shooterP = 0.015d;
//		public static final double shooterI = 0.0000d;
//		public static final double shooterD = 0d;
//		public static final int    shooterIz = 1000;
		
		public static final double shooterF = 0.0192d;
		public static final double shooterP = 0.008d;
		public static final double shooterI = 0.0002d;
		public static final double shooterD = 0.8d;
		public static final int    shooterIz = 8500;
		
		//Orlando
//		public static final double kickerF = 0.0582d;
//		public static final double kickerP = 0.08d;
//		public static final double kickerI = 0.0003d;
//		public static final double kickerD = 0d;
//		public static final int    kickerIz = 1000;
		
		public static final double kickerF = 0.0448d;
		public static final double kickerP = 0.01d;
		public static final double kickerI = 0.0001d;
		public static final double kickerD = 0d;
		public static final int    kickerIz = 3650;
		
		//needs to be sped up

		public static final double SHOOTER_RPM = 3150d - 150d; //3150
		public static final double KICKER_RPM = 1700d;  //1700
		
		public static final double SHOOTER_RPM_HIGH = SHOOTER_RPM + 150d;
		public static final double KICKER_RPM_HIGH = KICKER_RPM + 150d;
		
		public static final double BED_MS = 500;
		public static final double GATHER_MS = 75;
	}
	
	public final static class Ballarator {
		public static final int GATHER_CHAN = 15; //F:6, P:15
	}
	
	public final static class GearFlapper {
		public static final int GATHER_CHAN = 14; //F:12, P:14
		public static final int FLAP_CHAN = 12; //F: 7, P:12
		public static final int GATHERED_CHAN = 0; //F:0, P:0
		
		public static final double kP = 0.6;
		public static final double kI = 0;
		public static final double kD = 0;
		public static final double OFFSET = 0.110; //F:0.496, P:0.065
		//0 Deg is straight up and down.  Negative rotation is towards the floor
		public static final double DOWN_POS_DEG = -107d;
		public static final double STOW_POS_DEG = 2d;
	}
}
