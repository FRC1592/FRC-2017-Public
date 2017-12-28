package org.usfirst.frc.team1592.robot.commands.GearFlapper;

import org.usfirst.frc.team1592.robot.Robot;
import org.usfirst.frc.team1592.robot.subsystems.GearFlapper;

import edu.wpi.first.wpilibj.command.Command;

public class EjectGear extends Command {
	GearFlapper me = Robot.gearFlapper;

    public EjectGear() {
        requires(Robot.gearFlapper);
    }

    protected void initialize() {
    	me.setGearIn(false);
    }

    protected void execute() {
    	me.eject();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	me.stop();
    	System.out.println(Robot.match.getAutoTime());
    }

    protected void interrupted() {
    	end();
    }
}
