package org.usfirst.frc.team1592.robot.commands.Auto;

import org.usfirst.frc.team1592.arch.utils.Vector2D;
import org.usfirst.frc.team1592.robot.commands.Chassis.Drive2Point;
import org.usfirst.frc.team1592.robot.commands.Chassis.DriveOpenLoop;
import org.usfirst.frc.team1592.robot.commands.Chassis.Turn2HeadingMP;
import org.usfirst.frc.team1592.robot.commands.GearFlapper.GearEject;
import org.usfirst.frc.team1592.robot.commands.Trident.Shoot;
import org.usfirst.frc.team1592.robot.commands.Trident.StopShooter;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootAuto extends CommandGroup {
	Alliance alliance;
	
	public ShootAuto(Vector2D targetPos, double initAngle, double targetAng) {
		addSequential(new Turn2HeadingMP(initAngle),0.1);
		addSequential(new Shoot(),3.5);
		addSequential(new StopShooter());
		addSequential(new Drive2Point(targetPos.sub(new Vector2D(0, 1.5)),8.0,4.0));
		addSequential(new Turn2HeadingMP(targetAng),2.0);
//		addSequential(new CenterOnGear());
		addSequential(new Drive2Point(targetAng, -1.5d, 5.0, 1.7, true));
    	addSequential(new GearEject(),0.5);
		addSequential(new DriveOpenLoop(targetAng, 2*Math.signum(initAngle)), 1d); //FIXME: Hacky but may work ;)
		addSequential(new DriveOpenLoop(0, 5*Math.signum(initAngle)), 2d);
	}
	
	public enum ShootMode
	{
		ShootFirstLeftGear;
	}
}
