package org.usfirst.frc.team1592.robot.LEDs;

import org.usfirst.frc.team1592.robot.RobotMap;

public class UnderglowLEDs {
	private final int QUAD1A_START = 1;
	private final int QUAD1A_LEN = 5;
	private final int QUAD2_START = QUAD1A_START+QUAD1A_LEN;
	private final int QUAD2_LEN = 20;
	private final int QUAD3_START = QUAD2_START+QUAD2_LEN;
	private final int QUAD3_LEN = 22;
	private final int QUAD4_START = QUAD3_START+QUAD3_LEN;
	private final int QUAD4_LEN = 20;
	private final int QUAD1B_START = QUAD4_START+QUAD4_LEN;
	private final int QUAD1B_LEN = 5;
	
	
	LEDs leds = RobotMap.leds;

	public UnderglowLEDs() {
	}

	//Fill entire underglow
	public void setColor(int r, int g, int b) {
		leds.setColor(QUAD1A_START, QUAD1B_START+QUAD1B_LEN, r, g, b);
	}
}
