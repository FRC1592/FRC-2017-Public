package org.usfirst.frc.team1592.robot.commands.Chassis;

import org.usfirst.frc.team1592.arch.utils.Vector2D;
import org.usfirst.frc.team1592.robot.Constants;
import org.usfirst.frc.team1592.robot.Robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;

public class DriveAboutPoint extends Command {
	PivotQuad pivotCmd;
	Vector2D pivotPoint;
	private boolean wasFieldOriented;
	
    public DriveAboutPoint(PivotQuad pivot) {
        requires(Robot.chassis);
    	this.pivotCmd = pivot;
    }

    protected void initialize() {
    	double currentAngle = Robot.chassis.getAngle_Deg();
    	wasFieldOriented = Robot.chassis.isFieldOriented();
    	Robot.chassis.setFieldOriented(true);
    	
    	//Rotate ordinal directions into the field frame
    	switch(pivotCmd) {
    		case north:
    			currentAngle += 0;
    			break;
    		case east:
    			currentAngle += 90;
    			break;
    		case south:
    			currentAngle += 180;
    			break;
    		case west:
    			currentAngle += 270;
    			break;
    		default:
	    		new IllegalArgumentException("Invalid swerve pod selected");
	    		break;
    	}
    	//TODO: Not sure this works.  I think the angle could be wrapped multiple times
    	currentAngle = currentAngle>360?currentAngle-360:currentAngle;
    	
    	pivotPoint = calcPodFromAngle(currentAngle);
    }

    protected void execute() {
    	Robot.chassis.driveSwerve(0, 0, Robot.oi.driver.getX(Hand.kLeft), pivotPoint);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.chassis.driveSwerve(0,0,0);
    	//Return chassis field oriented to previous state just in case next commands assumes
    	Robot.chassis.setFieldOriented(wasFieldOriented);
    }

    protected void interrupted() {
    	end();
    }
    
    private Vector2D calcPodFromAngle(double angle) {
    	angle %= 360;
    	if(angle >= 315 || angle < 45)
    		return Constants.LFSwerve.LOC;
    	else if(angle >= 45 && angle < 135)
    		return Constants.RFSwerve.LOC;
    	else if(angle >= 135 && angle < 225)
    		return Constants.RBSwerve.LOC;
    	else if(angle >= 225 && angle < 315)
    		return Constants.LBSwerve.LOC;
    	else {
    		new IllegalArgumentException("Invalid angle, must be [0,360]");
    		return new Vector2D(0, 0);
    	}
    }
    
	public enum PivotQuad {
		north(0), east(1), south(2), west(3);
		
		public final int value;
		
		private PivotQuad(int value) {
			this.value = value;
		}
	}
}
