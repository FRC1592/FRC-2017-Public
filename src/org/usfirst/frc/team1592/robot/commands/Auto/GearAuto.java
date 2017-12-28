package org.usfirst.frc.team1592.robot.commands.Auto;

import org.usfirst.frc.team1592.arch.utils.Vector2D;
import org.usfirst.frc.team1592.robot.commands.Chassis.Drive2Point;
import org.usfirst.frc.team1592.robot.commands.Chassis.DriveOpenLoop;
import org.usfirst.frc.team1592.robot.commands.Chassis.Turn2HeadingMP;
import org.usfirst.frc.team1592.robot.commands.GearFlapper.GearEject;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearAuto extends CommandGroup {
	StartPos startPos;
	TargetPeg targetPeg;
	Alliance alliance;
	
	public GearAuto(Vector2D targetPos, double targetAng) {
		addSequential(new Turn2HeadingMP(180),1.1);
		addSequential(new Drive2Point(targetPos, 8.0, 4.0));
		addSequential(new Turn2HeadingMP(targetAng),2.0);
//		addSequential(new CenterOnGear());
		addSequential(new Drive2Point(targetAng, -1.5d, 8.0, 1.7, true));
    		addSequential(new GearEject(),0.5);
		addSequential(new DriveOpenLoop(targetAng, 2), 1d);
		addSequential(new DriveOpenLoop(0, 5), 2d);
	}
	
	public enum StartPos
	{
		Left(0), Center(1), Right(2);
		@SuppressWarnings("unused")
		private int value;
 
		private StartPos(int value) {
			this.value = value;
		}
	}
	
	public enum TargetPeg
	{
		Left(0), Center(1), Right(2);
		@SuppressWarnings("unused")
		private int value;
 
		private TargetPeg(int value) {
			this.value = value;
		}
	}
}
