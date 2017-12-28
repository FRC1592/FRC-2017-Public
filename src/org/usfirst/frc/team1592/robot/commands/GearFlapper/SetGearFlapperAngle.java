package org.usfirst.frc.team1592.robot.commands.GearFlapper;

import org.usfirst.frc.team1592.robot.Constants;
import org.usfirst.frc.team1592.robot.Robot;
import org.usfirst.frc.team1592.robot.PeriodicTasks.LedState.TankState;
import org.usfirst.frc.team1592.robot.subsystems.GearFlapper;

import edu.wpi.first.wpilibj.command.Command;

public class SetGearFlapperAngle extends Command {
	GearFlapper me = Robot.gearFlapper;
	
	double ang;

    public SetGearFlapperAngle(double ang) {
        requires(Robot.gearFlapper);
        this.ang = ang;
    }

    protected void initialize() {
    	if(ang == Constants.GearFlapper.STOW_POS_DEG && Robot.match.isTeleop())
    		Robot.ledState.setTankState(TankState.PixyFollow);
    	if(ang == Constants.GearFlapper.DOWN_POS_DEG && Robot.match.isTeleop())
    		Robot.ledState.setTankState(TankState.Orange);
    	me.setAngle_Deg(ang);
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {
    }

    protected void interrupted() {
    	end();
    }
}
