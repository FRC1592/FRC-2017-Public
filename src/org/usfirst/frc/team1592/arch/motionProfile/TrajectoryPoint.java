package org.usfirst.frc.team1592.arch.motionProfile;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class TrajectoryPoint 
{
	double time = 0;
	double vel = 0;
	double pos = 0;
	double accel=0;
	NumberFormat formatter = new DecimalFormat("#0.000");
	
	public TrajectoryPoint() {

	}
	
	public TrajectoryPoint(double time,double position,double velocity)
	{
		this.time=time;
		this.vel=velocity;
		this.pos=position;
	}
	
	public TrajectoryPoint(double time,double position,double velocity,double acceleration)
	{
		this.time=time;
		this.vel=velocity;
		this.pos=position;
		this.accel=acceleration;
	}
	
	public double getAcceleration() {
		return accel;
	}
	
	public void setAcceleration(double acceleration) {
		this.accel = acceleration;
	}
	
	@Override
	public String toString() {
		return  formatter.format(time) + ", " + formatter.format(pos) + ", " + formatter.format(vel) + ", " + formatter.format(accel);
	}
	
	public double getTime() {
		return time;
	}
	
	public void setTime(double time) {
		this.time = time;
	}
	
	public double getVelocity() {
		return vel;
	}
	
	public void setVelocity(double velocity) {
		this.vel = velocity;
	}
	
	public double getPosition() {
		return pos;
	}
	
	public void setPosition(double position) {
		this.pos = position;
	}

}
