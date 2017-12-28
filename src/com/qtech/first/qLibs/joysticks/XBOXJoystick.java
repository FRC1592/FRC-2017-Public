package com.qtech.first.qLibs.joysticks;

import org.usfirst.frc.team1592.arch.utils.Discontinuities;

import edu.wpi.first.wpilibj.Joystick;

/**
 * 
 * @author Thomas 'Squirrel' Muller
 * A wrapper for the XBOX 360 controller to make the controls more like a standard controller
 *
 */

public class XBOXJoystick extends Joystick
{
	/**
	 * Create a new XBOXJoystick object
	 * @param port USB port number
	 */
	public XBOXJoystick(int port)
	{
		super(port);
	}
	
	/**
	 * Get Right X axis of joystick
	 * @return U axis(-1, 1)
	 */
	public double getU()
	{
		return this.getRawAxis(4);
	}
	
	/**
	 * Get Right Y axis of joystick
	 * @return V axis(-1, 1)
	 */
	public double getV()
	{
		return this.getRawAxis(5);
	}
	
	/**
	 * @see edu.wpi.first.wpilibj.Joystick#getRawAxis(int)
	 */
	@Override
	public double getRawAxis(int axis)
	{
		return Discontinuities.dead(super.getRawAxis(axis), 0.1);
	}

	/**
	 * @see edu.wpi.first.wpilibj.Joystick#getRawButton(int)
	 */
	@Override
	public boolean getRawButton(int button)
	{
		switch(button)
		{
			case 1:
				return super.getRawButton(4);
			case 2:
				return super.getRawButton(2);
			case 3:
				return super.getRawButton(1);
			case 4:
				return super.getRawButton(3);
			case 5:
				return super.getRawButton(5);
			case 6:
				return super.getRawButton(6);
			case 7:
				return super.getRawAxis(2) > 0.1;
			case 8:
				return super.getRawAxis(3) > 0.1;
			case 9:
				return super.getRawButton(7);
			case 10:
				return super.getRawButton(8);
			default:
				return false;
		}
	}
}
