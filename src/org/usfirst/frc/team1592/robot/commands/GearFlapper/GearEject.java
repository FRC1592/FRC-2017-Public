package org.usfirst.frc.team1592.robot.commands.GearFlapper;

import org.usfirst.frc.team1592.robot.Constants;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearEject extends CommandGroup {
    
    public  GearEject() {
    	addSequential(new SetGearFlapperAngle(Constants.GearFlapper.DOWN_POS_DEG));
    	addSequential(new EjectGear());
    }
}
