package org.usfirst.frc.team1592.robot.LEDs;

import org.usfirst.frc.team1592.robot.RobotMap;

public class TankLEDs {
	private static final int RIGHT_START = 0;
	private static final int RIGHT_END = RIGHT_START + 10;
	private static final int TOP_START = RIGHT_END+1;
	private static final int TOP_END = TOP_START + 18;
	private static final int LEFT_START = TOP_END + 1;
	private static final int LEFT_END = LEFT_START + 10;
	
	
	LEDs leds = RobotMap.leds;

	public TankLEDs() {
	}

	public void fillSides(int r, int g, int b) {
		leds.setColor(RIGHT_START, RIGHT_END, r, g, b);
		leds.setColor(LEFT_START, LEFT_END, r, g, b);
	}

	public void fillTop(int r, int g, int b) {
		leds.setColor(RIGHT_START, LEFT_END, r, g, b);
	}

	public void setColorSides(int idx, int r, int g, int b) {
		leds.setColor(RIGHT_START + idx - 1, RIGHT_START+idx - 1, r, g, b);
		leds.setColor(LEFT_START + idx - 1, LEFT_START + idx - 1, r, g, b);
	}

	public void setColorTop(int idx, int r, int g, int b) {
		leds.setColor(TOP_START + idx - 1, TOP_START + idx - 1, r, g, b);
	}

	public void setColorSides(int start, int end, int r, int g, int b) {
		leds.setColor(RIGHT_START + start - 1, RIGHT_START + end - 1, r, g, b);
		leds.setColor(LEFT_START + start - 1, LEFT_START + end - 1, r, g, b);
	}

	public void setColorTop(int start, int end, int r, int g, int b) {
		leds.setColor(TOP_START + start - 1, TOP_START + end - 1, r, g, b);
	}
}
