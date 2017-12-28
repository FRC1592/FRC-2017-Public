package org.usfirst.frc.team1592.robot.commands.Auto;

import org.usfirst.frc.team1592.arch.utils.Vector2D;
import org.usfirst.frc.team1592.robot.commands.Chassis.Drive2Point;
import org.usfirst.frc.team1592.robot.commands.Chassis.Turn2HeadingMP;
import org.usfirst.frc.team1592.robot.commands.Trident.Shoot;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class HopperAuto extends CommandGroup {
	Alliance alliance;
	
	public HopperAuto(Vector2D targetPos, double targetAng, boolean shoot) {
		addSequential(new Turn2HeadingMP(180));
		addSequential(new Drive2Point(targetPos, 8.0, 4.0));
		//TODO: add conditional command for shoot
		addSequential(new Shoot());
	}
	
	public enum HopperMode
	{
		ShootHopper;
	}
}
