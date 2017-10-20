package org.usfirst.frc.team1155.robot;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import flanagan.integration.Integration;
import flanagan.integration.IntegralFunction;

public class DesCartesianPlane{
	private double x;
	private double y;
	private double velocity;
	
	ClockworkOrange theWatchmen;
	ADXRS450_Gyro chicken;
	ADXL345_I2C excel;
	
	public DesCartesianPlane(ClockworkOrange orange, ADXRS450_Gyro halal, ADXL345_I2C sheets) {
		theWatchmen = orange;
		chicken = halal;
		excel = sheets;
	}
	
	public void setX(double set) {
		x = set;
	}
	
	public void setY(double set) {
		y = set;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
		
	public void doesTheMath() {
		//Does the math cough cough bowen
	}
	
	public double getVelocity() {
		return velocity; //Shouldn't be needed, mostly for testing
	}
	
	public void setVelocity(double set) {
		velocity = set; //Shouldn't be needed, mostly for testing
	}
}