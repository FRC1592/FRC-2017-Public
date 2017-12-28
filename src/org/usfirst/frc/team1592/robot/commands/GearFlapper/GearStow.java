package org.usfirst.frc.team1592.robot.commands.GearFlapper;

import org.usfirst.frc.team1592.robot.Constants;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearStow extends CommandGroup {
    
    public  GearStow() {
    	addSequential(new SetGearFlapperAngle(Constants.GearFlapper.STOW_POS_DEG));
    	addSequential(new GatherGear());
    }
}
