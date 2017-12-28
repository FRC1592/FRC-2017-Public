package com.qtech.first.qLibs.sensors.encoders;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class MagEncoder implements PIDSource
{
	private CANTalon talon;
	private Encoder enoder;
	private Device device;
	PIDSourceType pidSourceType;
	
	int US_PER_ROT = 4202; //uS per rotation
	
	public enum Device
	{
		magEncTalon(30), magEncRIO(10);
		@SuppressWarnings("unused")
		private int value;
 
		private Device(int value) {
			this.value = value;
		}
	}
	
	/**
	 * Create a new MagEncoder object
	 * @param talon Talon that is hosting the encoder
	 */
	public MagEncoder(CANTalon talon)
	{
		this.device = Device.magEncTalon;
		this.talon = talon;
		this.talon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		this.pidSourceType = PIDSourceType.kDisplacement;
	}
	
	/**
	 * Create a new MagEncoder object
	 * @param a DIO id of encoder pin a
	 * @param b DIO id of encoder pin b
	 * @param pwm DIO of encoder pwm signal
	 */
	public MagEncoder(int a, int b, int pwm)
	{
		this.device = Device.magEncRIO;
		this.enoder = new Encoder(a, b);
		this.enoder.setDistancePerPulse(1 / 4096); //1 rev per 4096 pulses
	}
	
	/**
	 * Get relative distance traveled in feet
	 * @return Relative distance in feet
	 */
	public double getDistance()
	{
		switch(device)
		{
			case magEncTalon:
				return talon.getPosition();
			case magEncRIO:
				return enoder.getDistance();
			default:
				return 0;
		}
	}
	
	/**
	 * Get velocity in RPM
	 * @return Velocity in RPM
	 */
	public double getSpeed()
	{
		switch(device)
		{
			case magEncTalon:
				return talon.getSpeed();
			case magEncRIO:
				return enoder.getRate() * 60; //Convert RPS to RPM
			default:
				return 0;
		}
	}
	
	/**
	 * Gets 0-360 angle of encoder
	 * @return Angle (0-360)
	 */
	public double getAbsPos()
	{
		switch(device)
		{
			case magEncTalon:
				return talon.getPulseWidthRiseToFallUs() / US_PER_ROT;
			case magEncRIO:
				return -1; //TODO: Find a way to get uS from PWM
			default:
				return 0;
		}
	}
	
	/**
	 * Resets encoder position to 0
	 */
	public void reset()
	{
		switch(device)
		{
			case magEncTalon:
				talon.setEncPosition(0);
				break;
			case magEncRIO:
				enoder.reset();
				break;
			default:
				break;
		}
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		this.pidSourceType = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return pidSourceType;
	}

	@Override
	public double pidGet() {
		return getAbsPos();
	}
}
