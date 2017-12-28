package org.usfirst.frc.team1592.robot.LEDs;

import edu.wpi.first.wpilibj.DigitalOutput;

public class LedRing {
	DigitalOutput red;
	DigitalOutput grn;
	DigitalOutput blu;
	
	public LedRing(int redPin, int grnPin, int bluPin) {
		this.red = new DigitalOutput(redPin);
		this.grn = new DigitalOutput(grnPin);
		this.blu = new DigitalOutput(bluPin);
	}
	
	public void setColor(boolean r, boolean g, boolean b) {
		red.set(r);
		grn.set(g);
		blu.set(b);
	}
}
        