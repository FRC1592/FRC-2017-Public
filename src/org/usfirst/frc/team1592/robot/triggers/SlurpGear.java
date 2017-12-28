package org.usfirst.frc.team1592.robot.triggers;

import org.usfirst.frc.team1592.robot.Robot;

import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 *
 */
public class SlurpGear extends Trigger {
    
    public boolean get() {
        return !Robot.gearFlapper.canSeeGear() && Robot.gearFlapper.gearHasBeenGathered();
    }
}
