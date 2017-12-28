package org.usfirst.frc.team1592.robot.LEDs;

import java.io.ByteArrayOutputStream;

import org.usfirst.frc.team1592.robot.Constants;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.SerialPort;

public class LEDs {//extends SerialPort {
//	ByteArrayOutputStream buf = new ByteArrayOutputStream();
	private I2C ard;

	public LEDs() {
		this.ard = new I2C(Port.kMXP,84);
//		super(Constants.LEDs.BAUD, Port.kMXP);
	}
	
	public void setColor(int start, int end, int r, int g, int b) {
//		String buf = "";
//		buf += 'l';
//		buf += (char)start;
//		buf += (char)end;
//		buf += (char)r;
//		buf += (char)g;
//		buf += (char)b;
//		buf += '\n';
////		System.out.println(buf);
//		writeString(buf);
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public void setMode(char mode) {
		byte[] buf = new byte[1];
		buf[0] = (byte)mode;
		ard.write(buf[0], buf[0]);
//		ard.transaction(buf, 1, null, 0);
	}
}
