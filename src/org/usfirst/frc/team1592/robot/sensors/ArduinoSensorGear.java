package org.usfirst.frc.team1592.robot.sensors;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SensorBase;

public class ArduinoSensorGear extends SensorBase {

	private I2C m_i2c;
	private byte[] buffer;

	private int pixyErrP;
	private int lidarLeftD;
	private int lidarRightD;

	private boolean LIDAR_INSTALLED;

	public ArduinoSensorGear(boolean LIDAR) {  
		m_i2c = new I2C(I2C.Port.kMXP,0x65);
		buffer = new byte[6];
		initArduino();
		pixyErrP = 0;
		LIDAR_INSTALLED = LIDAR;
		if(LIDAR_INSTALLED)
		{
			lidarLeftD = 0;
			lidarRightD = 0;
		}
		else
		{
			lidarLeftD = -1;
			lidarRightD = -1;
		}
	}

	public void initArduino() {
		//Nothing to do
	}

	public void getBuffer() {
		int lastLeftD = lidarLeftD;
		int lastRightD = lidarRightD;
		m_i2c.readOnly(buffer, 6); 	
        
		pixyErrP =(int)(buffer[0] << 8 | Byte.toUnsignedInt(buffer[1]));
        //System.out.println(pixyErrP);
		if(LIDAR_INSTALLED)
		{
			lidarLeftD = (int)(buffer[2] << 8) + Byte.toUnsignedInt(buffer[3]);
			if (lidarLeftD <= 0) lidarLeftD = lastLeftD;
			lidarRightD = (int)(buffer[4] << 8) + Byte.toUnsignedInt(buffer[5]);
			if (lidarRightD <= 0) lidarRightD = lastRightD;
		}
	}

	public int getLidar() {
		return lidarLeftD;
	}

	public int getRightLidar() {
		return lidarRightD;
	}

	public int getPixy() {
		return pixyErrP;
	}

	public void initDefaultCommand() {
		// nothing to do
	}

}
