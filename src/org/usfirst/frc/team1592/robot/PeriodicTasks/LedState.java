package org.usfirst.frc.team1592.robot.PeriodicTasks;

import org.usfirst.frc.team1592.arch.utils.PeriodicTask;
import org.usfirst.frc.team1592.robot.RobotMap;

public class LedState extends PeriodicTask {
	UnderglowState underState;
	TankState tankState;
	
	public LedState() {
		super(100); // period in ms
	}
    
	@Override
    public void run() {
		if(!RobotMap.gearGathered.get())
			RobotMap.leds.setMode('g');
		else
			RobotMap.leds.setMode('n');
//		RobotMap.tankLeds.fillTop(255, 60, 0);
//		RobotMap.tankLeds.fillSides(0, 0, 255);
//		if(Robot.match.getTeleopTime() > 195)
//			setTankState(TankState.Last20);
    }
	
	public void setUnderglowState(UnderglowState state) {
		switch(state) {
		case RedAl:
			RobotMap.underLeds.setColor(255, 0, 0);
			break;
		case BluAl:
			RobotMap.underLeds.setColor(0, 0, 255);
			break;
		}
	}
	
	public void setTankState(TankState state) {
		switch(state) {
		case Orange:
			RobotMap.tankLeds.fillTop(255, 60, 0);
		case PixyFollow:
			double pos = (double)RobotMap.gearArduino.getPixy() / 319d;
			int ledPos = (int)Math.floor(pos * 18d);
			if(ledPos < 1)
				ledPos = 1;
			if(ledPos > 17)
				ledPos = 17;
			
			RobotMap.tankLeds.setColorTop(ledPos-1, ledPos+1, 0, 255, 0);
		case Last20:
		}
	}
	
	public enum UnderglowState
	{
		RedAl(0), BluAl(1);
		@SuppressWarnings("unused")
		private int value;
 
		private UnderglowState(int value) {
			this.value = value;
		}
	}
	
	public enum TankState
	{
		Orange(0), PixyFollow(1), Last20(2);
		@SuppressWarnings("unused")
		private int value;
 
		private TankState(int value) {
			this.value = value;
		}
	}
}

