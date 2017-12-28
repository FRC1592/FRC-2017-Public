package org.usfirst.frc.team1592.robot.commands.GearFlapper;

import org.usfirst.frc.team1592.robot.Robot;
import org.usfirst.frc.team1592.robot.subsystems.GearFlapper;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GatherGear extends Command {
	GearFlapper me = Robot.gearFlapper;

    public GatherGear() {
        requires(Robot.gearFlapper);
    }

    protected void initialize() {
    	Robot.oi.driver.setRumble(RumbleType.kLeftRumble, 1);
    	Robot.oi.manip.setRumble(RumbleType.kLeftRumble, 1);
    	Robot.gearFlapper.setGearIn(false);
    }

    protected void execute() {
    	if(me.canSeeGear()) {
        	Robot.oi.driver.setRumble(RumbleType.kLeftRumble, 0);
        	Robot.oi.manip.setRumble(RumbleType.kLeftRumble, 0);
    		me.stop();
    		me.setGearIn(true);
    	} else {
        	Robot.oi.driver.setRumble(RumbleType.kLeftRumble, 1);
        	Robot.oi.manip.setRumble(RumbleType.kLeftRumble, 1);
    		me.gather();
    	}
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.oi.driver.setRumble(RumbleType.kLeftRumble, 0);
    	Robot.oi.manip.setRumble(RumbleType.kLeftRumble, 0);
    	me.stop();
    }

    protected void interrupted() {
    	end();
    }
}
