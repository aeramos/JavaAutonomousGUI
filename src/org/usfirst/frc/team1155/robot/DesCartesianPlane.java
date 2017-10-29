package org.usfirst.frc.team1155.robot;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class DesCartesianPlane{
	private double x = 0;
	private double y = 0;
	private double prevVx = 0;
	private double prevVy = 0;
	private double vx = 0;
	private double vy = 0;
	private double prevAx = 0;
	private double prevAy = 0;
	private double ax;
	private double ay;
	private double dt;
	
	public Timer timer;
	public ADXRS450_Gyro gyro;
	public ADXL345_I2C accelerometer;
	
	public DesCartesianPlane(Timer aTimer, ADXRS450_Gyro aGyro, ADXL345_I2C anAccelerometer) {
		timer = aTimer;
		gyro = aGyro;
		accelerometer = anAccelerometer;
	}
	public void updatePosition() {
		ax = accelerometer.getX();
		ay = accelerometer.getY();
		dt = timer.getTimeDifference();

		vx += 0.5 * dt * (prevAx + ax);
		vy += 0.5 * dt * (prevAy + ay);

		x += 0.5 * dt * (prevVx + vx);
		y += 0.5 * dt * (prevVy + vy);

		prevAx = ax;
		prevAy = ay;

		prevVx = vx;
		prevVy = vy;

	}
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}

	public double angleRequiredToTurn(double xDistance, double yDistance, double radius) { //Basically Bowen	

		double angle = Math.toDegrees(Math.asin(xDistance/radius));

		return yDistance >= 0 ? angle : angle + 90;

	}
	
	public double getUsefulAngle() {
		return gyro.getAngle() > 180 ? gyro.getAngle() - 360 : gyro.getAngle();

	}
}
