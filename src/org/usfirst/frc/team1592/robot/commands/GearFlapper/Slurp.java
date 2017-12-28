package org.usfirst.frc.team1592.robot.commands.GearFlapper;

import org.usfirst.frc.team1592.robot.Robot;
import org.usfirst.frc.team1592.robot.subsystems.GearFlapper;

import edu.wpi.first.wpilibj.command.Command;

public class Slurp extends Command {
	GearFlapper me = Robot.gearFlapper;

    public Slurp() {
        requires(Robot.gearFlapper);
    }

    protected void initialize() {
    }

    protected void execute() {
    	me.gather();
    }

    protected boolean isFinished() {
        return me.canSeeGear();
    }

    protected void end() {
    	me.stop();
    }

    protected void interrupted() {
    	end();
    }
}
