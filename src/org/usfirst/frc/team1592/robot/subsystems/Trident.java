package org.usfirst.frc.team1592.robot.subsystems;

import org.usfirst.frc.team1592.robot.Constants;
import org.usfirst.frc.team1592.robot.RobotMap;
import org.usfirst.frc.team1592.robot.commands.Idle;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Trident extends Subsystem {
	private static CANTalon tridentMaster = RobotMap.tridentMaster;
	private static CANTalon kicker = RobotMap.kicker;
	private static CANTalon liftBelt = RobotMap.liftBelt;
	private static CANTalon bedBelt = RobotMap.bedRoller;

	double shooterRPM;
	double kickerRPM;
	
	public Trident() {
//		RobotMap.dataLogger.registerDoubleStream("ShooterVelocity", "RPM", ()->getShooterRpm(),new DecimalFormat("#.#"));
//		RobotMap.dataLogger.registerDoubleStream("ShooterError", "counts/100ms", ()->RobotMap.tridentMaster.getClosedLoopError(),new DecimalFormat("#.#"));
//		RobotMap.dataLogger.registerLongStream("ShooterIAccum", "counts/100", ()->RobotMap.tridentMaster.GetIaccum());
//		RobotMap.dataLogger.registerDoubleStream("KickerVelocity", "RPM", ()->getKickerRpm(),new DecimalFormat("#.#"));
//		RobotMap.dataLogger.registerDoubleStream("KickerSetpoint", "RPM", ()->RobotMap.kicker.getSetpoint(),new DecimalFormat("#"));
//		RobotMap.dataLogger.registerDoubleStream("ShooterSetpoint", "RPM", ()->RobotMap.tridentMaster.getSetpoint(),new DecimalFormat("#"));
////		//Note: inverting error and iaccum since loop output is inverted
//		RobotMap.dataLogger.registerDoubleStream("KickerError", "counts/100ms", ()->-RobotMap.kicker.getClosedLoopError(),new DecimalFormat("#.#"));
//		RobotMap.dataLogger.registerLongStream("KickerIAccum", "counts/100", ()->-RobotMap.kicker.GetIaccum());
//		RobotMap.dataLogger.registerDoubleStream("ShooterCurrent", "A", ()->RobotMap.tridentMaster.getOutputCurrent() + RobotMap.tridentSlave1.getOutputCurrent(),new DecimalFormat("#.#"));
//		RobotMap.dataLogger.registerDoubleStream("KickerCurrent", "A", ()->RobotMap.kicker.getOutputCurrent(),new DecimalFormat("#.#"));
//		RobotMap.dataLogger.registerDoubleStream("ShooterVoltage", "V", ()->RobotMap.tridentMaster.getOutputVoltage(),new DecimalFormat("#.#"));
//		RobotMap.dataLogger.registerDoubleStream("KickerVoltage", "V", ()->RobotMap.kicker.getOutputVoltage(),new DecimalFormat("#.#"));
//		
//		RobotMap.dataLogger.registerDoubleStream("LifterSpeed", "RPM", ()->RobotMap.liftBelt.getSpeed(),new DecimalFormat("#.#"));
    	
	}
	
    public void initDefaultCommand() {
    	setDefaultCommand(new Idle(this));
    }
    
    public double getShooterRpm() {
    	return -tridentMaster.getSpeed();
    }
    
    public double getKickerRpm() {
    	return -kicker.getSpeed();
    }
    
    public void setShooterRPM(double rpm) {
    	shooterRPM = rpm;
    	if(shooterRPM == 0) {
    		tridentMaster.disableControl();
    		tridentMaster.set(0);
    	} else {
    		tridentMaster.enableControl();
    		tridentMaster.set(shooterRPM);
    	}
    }
    
    public void setKickerRPM(double rpm) {
    	kickerRPM = rpm;
    	if(kickerRPM == 0) {
    		kicker.disableControl();
    		kicker.set(0);
    	} else {
    		kicker.enableControl();
    		kicker.set(kickerRPM);
    	}
    }
    
    public void setBed(double cmd) {
    	bedBelt.set(cmd);
    }
    
    public void setLift(double cmd) {
    	liftBelt.set(cmd);
    }
    
    public void stopShooter() {
    	setShooterRPM(0);
    }
    
    public void stopKicker() {
    	setKickerRPM(0);
    }
    
    public void stopBed() {
    	setBed(0d);
    }
    
    public void stopLift() {
    	setLift(0);
    }
    
    public boolean shooterAtSpeed() {
    	return getShooterRpm() > shooterRPM - 650;
    }
    
    public boolean kickerAtSpeed() {
//    	System.out.println(getKickerRpm());
    	return getKickerRpm() > kickerRPM - 650;
    }
    
    static {
    	//TODO: think these 2 inversions could be replaced by reverseSensor
    	RobotMap.tridentMaster.reverseOutput(true);
		RobotMap.tridentMaster.setInverted(true);
		RobotMap.tridentMaster.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		RobotMap.tridentMaster.changeControlMode(TalonControlMode.Speed);
		RobotMap.tridentMaster.configNominalOutputVoltage(+0.0d, -0.0d);
		RobotMap.tridentMaster.configPeakOutputVoltage(+12.0d, -12.0d);
		RobotMap.tridentMaster.setProfile(0);
		RobotMap.tridentMaster.setF(Constants.Trident.shooterF);
		RobotMap.tridentMaster.setP(Constants.Trident.shooterP);
		RobotMap.tridentMaster.setI(Constants.Trident.shooterI);
		RobotMap.tridentMaster.setD(Constants.Trident.shooterD);
		RobotMap.tridentMaster.setIZone(Constants.Trident.shooterIz);
		RobotMap.tridentMaster.enableBrakeMode(false);
    	
		RobotMap.tridentSlave1.changeControlMode(TalonControlMode.Follower);
		RobotMap.tridentSlave1.set(tridentMaster.getDeviceID());
		RobotMap.tridentSlave1.enableBrakeMode(false);

    	RobotMap.kicker.setInverted(true);
		RobotMap.kicker.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		RobotMap.kicker.changeControlMode(TalonControlMode.Speed);
		RobotMap.kicker.configNominalOutputVoltage(+0.0d, -0.0d);
		RobotMap.kicker.configPeakOutputVoltage(+12.0d, -12.0d);
		RobotMap.kicker.setProfile(0);
		RobotMap.kicker.setF(Constants.Trident.kickerF);
		RobotMap.kicker.setP(Constants.Trident.kickerP);
		RobotMap.kicker.setI(Constants.Trident.kickerI);
		RobotMap.kicker.setD(Constants.Trident.kickerD);
		RobotMap.kicker.setIZone(Constants.Trident.kickerIz);
		RobotMap.kicker.enableBrakeMode(false);
    	
		RobotMap.liftBelt.setInverted(true);
		RobotMap.liftBelt.setVoltageRampRate(48);
		RobotMap.liftBelt.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		
		RobotMap.bedRoller.setVoltageRampRate(48);
    }
}

