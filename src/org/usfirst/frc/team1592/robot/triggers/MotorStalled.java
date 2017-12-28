package org.usfirst.frc.team1592.robot.triggers;


import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.buttons.Trigger;

public class MotorStalled extends Trigger
{
	private CANTalon m_motor;
	private int m_stallCurrent;
	private int m_count;
	private int m_stallTime; //sec
	boolean motorStalled;

	public MotorStalled(CANTalon motor, int stallCurrent, int stallTimeMS)
	{
		m_motor = motor;
		m_stallCurrent = stallCurrent;
		m_count = 0;
		m_stallTime = stallTimeMS;
	}

	public boolean get()
	{
		//If current is greater than stall current, increment counter
		if(m_motor.getOutputCurrent() >= m_stallCurrent)
		{
			m_count += 1;
			System.out.println("Motor Stall Count: " + m_count * 20 + "ms");
			//count is multiplied by 0.02 sec/cycle * 1000 millisec/sec = 20
			if(m_count * 20 >= m_stallTime)
			{
				System.out.println("Motor Stalled");
				//Write to data file
				motorStalled = true;
				return true;
			}
		}
		else m_count = 0; //if current drops below stall limit, reset counter
			motorStalled = false;
			return false;
	}
	public boolean getMotorStalled()
	{
		return motorStalled;
	}
}