package com.qtech.first.qLibs.sensors.lidar;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.Timer;

/**
 * LidarLite:
 * @author Thomas
 *
 */

public class LidarLite extends SensorBase {
    private I2C lidar;
    private int lastValidDist = 0;
    
    public LidarLite() {
    	this(Port.kMXP); //Default to MXP
    }
    
    /**
     * Creates a LiarLite object
     * @param port I2C port to use
     */
    public LidarLite(Port port) {
    	this.lidar = new I2C(port,0x62);
    }
    
    /**
     * Get distance from the lidar
     * @return Distance in inches?
     */
	public int getDistance() {
		byte[] buffer;
		buffer = new byte[2];
	
		lidar.write(0x00, 0x04);
		Timer.delay(0.02);
		lidar.read(0x8f, 2, buffer);  	
		
		int dist = (int)Integer.toUnsignedLong(buffer[0] << 8) + Byte.toUnsignedInt(buffer[1]);
		if (dist > 0) lastValidDist = dist;
		return 	lastValidDist;
	}
}