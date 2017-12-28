package org.usfirst.frc.team1592.robot.subsystems;

import org.usfirst.frc.team1592.robot.Constants;
import org.usfirst.frc.team1592.robot.RobotMap;
import org.usfirst.frc.team1592.robot.commands.Idle;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

public class GearFlapper extends Subsystem {
	CANTalon gather = RobotMap.gearGather;
	CANTalon flap = RobotMap.gearFlap;
	
	DigitalInput gathered = RobotMap.gearGathered;
	
	private boolean gearIn;

	public GearFlapper() {
		flap.set(0);
	}
	
    public void initDefaultCommand() {
        setDefaultCommand(new Idle(this));
    }
    
    public void gather() {
    	gather.set(-1d);
    }
    
    public void eject() {
    	gather.set(1d);
    }
    
    public void stop() {
    	gather.set(0d);
    }
    
    public void setAngle_Deg(double ang) {
    	flap.set(ang/360d);
    	//flap.set((ang/360d)+Constants.GearFlapper.OFFSET);
    }

    
	public double getAngleDeg() {
		return flap.getPosition() * 360d;
	}
	
	public double getAbsEncAngle_Revs() {
		return flap.getPulseWidthPosition()/4096d;
	}
    
    public boolean canSeeGear() {
    	return !gathered.get();
    }
    
    public void setGearIn(boolean in) {
    	this.gearIn = in;
    }
    
    public boolean gearHasBeenGathered() {
    	return gearIn;
    }
    
    static {
    	//========Basic Settings==========
    	RobotMap.gearFlap.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
    	RobotMap.gearFlap.changeControlMode(TalonControlMode.Position);
    	RobotMap.gearFlap.setInverted(false);
    	RobotMap.gearFlap.reverseOutput(false);
    	RobotMap.gearFlap.setP(Constants.GearFlapper.kP);
    	RobotMap.gearFlap.setI(Constants.GearFlapper.kI);
    	RobotMap.gearFlap.setD(Constants.GearFlapper.kD);
    	
    	//=========Initialize encoder========
    	double flapInitial_Revs = RobotMap.gearFlap.getPulseWidthPosition()/4096d - Constants.GearFlapper.OFFSET;
		//Unwrap to (-0.5,0.5] assuming no more than one wrap
		if (flapInitial_Revs > 0.5) {
			flapInitial_Revs -= 1; }
		else if (flapInitial_Revs <= -0.5) {
			flapInitial_Revs += 1; }
		//Just in case someone got the bias wrong, make sure it's within the physical constraints of the system
		/*
		if (flapInitial_Revs < 0 || flapInitial_Revs > 0.7) 
		{
			DriverStation.reportError("Arm bias or init is not right yo", false);
			flapInitial_Revs = 0;
		}
		*/
		//set initial position of RELATIVE encoder in Talon
		RobotMap.gearFlap.setPosition(flapInitial_Revs); //[revs]
    }
}

