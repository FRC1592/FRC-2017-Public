package org.usfirst.frc.team1592.robot.commands.Auto;

import org.usfirst.frc.team1592.arch.utils.Vector2D;
import org.usfirst.frc.team1592.robot.Robot;
import org.usfirst.frc.team1592.robot.commands.Idle;
import org.usfirst.frc.team1592.robot.commands.Auto.GearAuto.StartPos;
import org.usfirst.frc.team1592.robot.commands.Auto.GearAuto.TargetPeg;
import org.usfirst.frc.team1592.robot.commands.Auto.HopperAuto.HopperMode;
import org.usfirst.frc.team1592.robot.commands.Auto.ShootAuto.ShootMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class FakeAuto {
	AutoMode mode;
	
	public FakeAuto(AutoMode mode) {
		this.mode = mode;
	}
	
	public Command getRealCommand() {
		switch(mode) {
		case GearLL:
			return getGearCmd(StartPos.Left, TargetPeg.Left);
		case GearLC:
			return getGearCmd(StartPos.Left, TargetPeg.Center);
		case GearLR:
			return getGearCmd(StartPos.Left, TargetPeg.Right);
		case GearCL:
			return getGearCmd(StartPos.Center, TargetPeg.Left);
		case GearCC:
			return getGearCmd(StartPos.Center, TargetPeg.Center);
		case GearCR:
			return getGearCmd(StartPos.Center, TargetPeg.Right);
		case GearRL:
			return getGearCmd(StartPos.Right, TargetPeg.Left);
		case GearRC:
			return getGearCmd(StartPos.Right, TargetPeg.Center);
		case GearRR:
			return getGearCmd(StartPos.Right, TargetPeg.Right);
//		case HopperL:
//			return getHopperCmd(StartPos.Left);
//		case HopperC:
//			return getHopperCmd(StartPos.Center);
		case ShootLGear:
			return getShootCmd(ShootMode.ShootFirstLeftGear);
		case ShootHopper:
			return getHopperCmd(HopperMode.ShootHopper);
//		case HopperC:
//			return getHopperCmd(StartPos.Center);
		case DriveOLS:
			return getDriveLineCmd(StartPos.Left);
		case DriveOLC:
			return getDriveLineCmd(StartPos.Center);
		default:
			return new Idle(null);
		}
	}
	
	private Command getGearCmd(StartPos startPos, TargetPeg targetPeg) {
		Vector2D targetPos = new Vector2D(0, 0);
		double targetAng = 0;
		
		int teamInv;
		if(DriverStation.getInstance().getAlliance() == Alliance.Blue)
			teamInv = 1;
		else
			teamInv = -1;
		

		Vector2D startLocation;
		switch(startPos) {
		case Left:
			startLocation = new Vector2D(teamInv*-6.25, 0);
			break;
		case Center:
			startLocation = new Vector2D(0, 0);
			break;
		case Right:
			startLocation = new Vector2D(teamInv*6.0, 0);
			break;
		default:
			startLocation = new Vector2D(0,0);
		
		}
		//NOTE: 90deg converts from (right, fwd, up) frame to (fwd, left, up) frame
		Robot.nav.setPosition(startLocation.rotateByAngle(90.0));
		switch(targetPeg) {
		case Left:
			targetPos = new Vector2D(teamInv*-5.25, 8.25);
			targetAng = 120;
			break;
		case Center:
			targetPos = new Vector2D(teamInv*0, 4.85);
			targetAng = 0;
			break;
		case Right:
			targetPos = new Vector2D(teamInv*5.25, 8.25);
			targetAng = 240;
			break;
		}
		System.out.println(targetPos.rotateByAngle(90.0)+", "+targetAng);
		//NOTE: 90deg converts from (right, fwd, up) frame to (fwd, left, up) frame
		return new GearAuto(targetPos.rotateByAngle(90.0), targetAng);
	}
	
	private Command getHopperCmd(HopperMode opt) {
		Vector2D targetPos = new Vector2D(0, 0);
		double targetAng = 0;
		
		int teamInv;
		if(DriverStation.getInstance().getAlliance() == Alliance.Blue)
			teamInv = 1;
		else
			teamInv = -1;
		
		switch(opt) {
			case ShootHopper:
				Robot.nav.setPosition(teamInv*-6.0, 0);
				targetPos = new Vector2D(-14.5, 7.10);
				targetAng = 180;
				break;
			}
		//NOTE: 90deg converts from (right, fwd, up) frame to (fwd, left, up) frame
		return new HopperAuto(targetPos.rotateByAngle(90.0), targetAng, true);
	}
	
	private Command getShootCmd(ShootMode opt) {
		Vector2D targetPos = new Vector2D(0, 0);
		double targetAng = 0;
		
		int teamInv;
		if(DriverStation.getInstance().getAlliance() == Alliance.Blue)
			teamInv = 1;
		else
			teamInv = -1;
		
		Robot.chassis.setRobotAngle(teamInv*96); //Blue:Red
		Vector2D startLocation = new Vector2D(0,0);
		switch(opt) {
		case ShootFirstLeftGear:
			startLocation = new Vector2D(teamInv*-6.0, 0);
			targetPos = new Vector2D(teamInv*-5.00, 9.25);
			targetAng = teamInv*120;
			break;
//		case Center:
//			Robot.nav.setPosition(0, 0);
//			break;
//		case Right:
//			Robot.nav.setPosition(teamInv*6.1, 0);
//			break;
		}
		//NOTE: 90deg converts from (right, fwd, up) frame to (fwd, left, up) frame
		Robot.nav.setPosition(startLocation.rotateByAngle(90));
		return new ShootAuto(targetPos.rotateByAngle(90.0), teamInv*96, targetAng);
	}
	
	private Command getDriveLineCmd(StartPos startPos) {
		Vector2D targetPos = new Vector2D(0, 0);
		double targetAng = 0;
		
		int teamInv;
		if(DriverStation.getInstance().getAlliance() == Alliance.Blue)
			teamInv = 1;
		else
			teamInv = -1;
		
		switch(startPos) {
		case Left:
			targetPos = new Vector2D(teamInv*0, 18);
			break;
		case Center:
			targetPos = new Vector2D(teamInv*7, 18);
			break;
		case Right:
			System.out.println("You failed");
			break;
		}
		//NOTE: 90deg converts from (right, fwd, up) frame to (fwd, left, up) frame
		return new HopperAuto(targetPos.rotateByAngle(90.0), targetAng, false);
	}
	
	public enum AutoMode
	{
		GearLL, GearLC, GearLR,
		GearCL, GearCC, GearCR,
		GearRL, GearRC, GearRR,
		HopperL, HopperC,
		ShootLGear, ShootHopper,
		DriveOLS, DriveOLC,;
	}
}
