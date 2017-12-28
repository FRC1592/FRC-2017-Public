/**
 * This file contains the device mapping for the robot.  All hardware devices are
 * defined here and fed to the subsystems.  Ideally the 
 */
package org.usfirst.frc.team1592.robot;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.usfirst.frc.team1592.arch.swerve.SwerveDrive;
import org.usfirst.frc.team1592.arch.swerve.SwervePod;
import org.usfirst.frc.team1592.arch.tm.SimplotStream;
import org.usfirst.frc.team1592.arch.tm.Telemetry;
import org.usfirst.frc.team1592.robot.LEDs.LEDs;
import org.usfirst.frc.team1592.robot.LEDs.LedRing;
import org.usfirst.frc.team1592.robot.LEDs.TankLEDs;
import org.usfirst.frc.team1592.robot.LEDs.UnderglowLEDs;
import org.usfirst.frc.team1592.robot.sensors.ArduinoSensorGear;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SPI;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    public static AHRS navX = new AHRS(SPI.Port.kMXP);
    
    public static LEDs leds = new LEDs();
    public static UnderglowLEDs underLeds = new UnderglowLEDs();
    public static TankLEDs tankLeds = new TankLEDs();
    
    public static LedRing ledRing = new LedRing(Constants.LEDs.RED_CHAN, Constants.LEDs.GRN_CHAN, Constants.LEDs.BLU_CHAN);
    
    //Swerve Pods
    public static SwervePod leftFrontPod = new SwervePod(Constants.LFSwerve.DRIVE_CHAN, Constants.LFSwerve.ROTATE_CHAN, Constants.LFSwerve.LOC, Constants.LFSwerve.OFFSET, Constants.WHEEL_RAD, Constants.rotatePodPID, Constants.ENC2AXLE_RATIO);
    public static SwervePod rightFrontPod = new SwervePod(Constants.RFSwerve.DRIVE_CHAN, Constants.RFSwerve.ROTATE_CHAN, Constants.RFSwerve.LOC, Constants.RFSwerve.OFFSET, Constants.WHEEL_RAD, Constants.rotatePodPID, Constants.ENC2AXLE_RATIO);
    public static SwervePod leftBackPod = new SwervePod(Constants.LBSwerve.DRIVE_CHAN, Constants.LBSwerve.ROTATE_CHAN, Constants.LBSwerve.LOC, Constants.LBSwerve.OFFSET, Constants.WHEEL_RAD, Constants.rotatePodPID, Constants.ENC2AXLE_RATIO);
    public static SwervePod rightBackPod = new SwervePod(Constants.RBSwerve.DRIVE_CHAN, Constants.RBSwerve.ROTATE_CHAN, Constants.RBSwerve.LOC, Constants.RBSwerve.OFFSET, Constants.WHEEL_RAD, Constants.rotatePodPID, Constants.ENC2AXLE_RATIO);
    
    //Swerve Drive Subsystem
    public static SwerveDrive swerveDrive = new SwerveDrive(leftFrontPod,rightFrontPod,leftBackPod,rightBackPod);
    
    //Trident
    public static CANTalon tridentMaster = new CANTalon(Constants.Trident.MASTER_CHAN);
    public static CANTalon tridentSlave1 = new CANTalon(Constants.Trident.SLAVE_CHAN);
    public static CANTalon kicker = new CANTalon(Constants.Trident.KICKER_CHAN);
    public static CANTalon liftBelt = new CANTalon(Constants.Trident.LIFT_CHAN);
    public static CANTalon bedRoller = new CANTalon(Constants.Trident.BED_CHAN);
    
    //Ballarator
    public static CANTalon gather = new CANTalon(Constants.Ballarator.GATHER_CHAN);
    
    //Gear Flapper
    public static CANTalon gearGather = new CANTalon(Constants.GearFlapper.GATHER_CHAN);
    public static CANTalon gearFlap = new CANTalon(Constants.GearFlapper.FLAP_CHAN);
    public static DigitalInput gearGathered = new DigitalInput(Constants.GearFlapper.GATHERED_CHAN);
    
    // Boiler Pixy LIDAR
//    public static ArduinoSensor boilerArduino = new ArduinoSensor(true);
    
    // Gear Pixy LIDAR
    public static ArduinoSensorGear gearArduino = new ArduinoSensorGear(true);
    
	//Data Logger
	public static Telemetry dataLogger = new Telemetry();
	public static SimplotStream dataStream = new SimplotStream(Paths.get("/u/logs/" + 
			LocalDateTime.now().format(DateTimeFormatter.ofPattern("uu_MM_dd_HH_mm_ss")) + ".hpl"));
	
	static {
		dataLogger.addTelemetryStream("Data", dataStream);
	}
	
}
