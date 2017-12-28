package org.usfirst.frc.team1592.robot.commands.Trident;

import org.usfirst.frc.team1592.robot.Robot;
import org.usfirst.frc.team1592.robot.RobotMap;

import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SnugClimb extends Command {
    public SnugClimb() {
    	requires(Robot.trident);
    }

    protected void initialize() {
//    	RobotMap.dataLogger.outputEvent("Snugged Climb");
    	
    	Robot.oi.climbLimit.whenActive(new StopClimber());
    	
    	//Change to %vbus and enable limits, starrt climb at full speed
    	RobotMap.tridentMaster.enableLimitSwitch(false, false);
    	RobotMap.tridentMaster.changeControlMode(TalonControlMode.PercentVbus);
    }

    protected void execute() {
    	Robot.trident.setShooterRPM(-0.7d);
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
