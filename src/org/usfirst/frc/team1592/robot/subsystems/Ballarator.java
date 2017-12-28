package org.usfirst.frc.team1592.robot.subsystems;

import org.usfirst.frc.team1592.robot.RobotMap;
import org.usfirst.frc.team1592.robot.commands.Idle;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Ballarator extends Subsystem {
	CANTalon gather = RobotMap.gather;
	
    public void initDefaultCommand() {
    	setDefaultCommand(new Idle(this));
    }
    
    public void setGather(double cmd) {
    	gather.set(cmd);
    }
    
    public void gather() {
    	gather.set(1d);
    }
    
    public void eject() {
    	gather.set(-1d);
    }
    
    public void stop() {
    	gather.set(0d);
    }
}

