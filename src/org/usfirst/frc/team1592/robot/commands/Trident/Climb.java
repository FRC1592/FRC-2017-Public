package org.usfirst.frc.team1592.robot.commands.Trident;

import org.usfirst.frc.team1592.robot.Robot;
import org.usfirst.frc.team1592.robot.RobotMap;

import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Climb extends Command {
    public Climb() {
    	requires(Robot.trident);
    }

    protected void initialize() {
//    	RobotMap.dataLogger.outputEvent("Started Climb");
    	
    	Robot.oi.climbLimit.whenActive(new StopClimber());
    	
    	//Change to %vbus and enable limits, starrt climb at full speed
    	RobotMap.tridentMaster.enableLimitSwitch(true, true);
    	RobotMap.tridentMaster.changeControlMode(TalonControlMode.PercentVbus);
    }

    protected void execute() {
    	Robot.trident.setShooterRPM(-1d);
    }

    protected boolean isFinished() {
    	return false;
    }

    protected void end() {
    	//Return to speed control mode
    	RobotMap.tridentMaster.changeControlMode(TalonControlMode.Speed);
    }

    protected void interrupted() {
    	end();
    }
}
