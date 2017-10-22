package org.usfirst.frc.team1155.robot;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import flanagan.integration.Integration;
import flanagan.integration.IntegralFunction;

public class DesCartesianPlane{
	private double x;
	private double y;
	private double velocity;
	
	public ClockworkOrange theWatchmen;
	public ADXRS450_Gyro chicken;
	public ADXL345_I2C excel;
	
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
	
	public double distanceBetweenPoints(double x1, double y1, double x2, double y2) {
		return Math.hypot(x2 - x1, y2 - y1);
	}
	
	public double distanceBetweenCoords(double x1y1, double x2y2) {
		return x2y2 - x1y1;
	}

	public double angleRequiredToTurn(double xDistance, double yDistance, double radius) { //Basically Bowen
		if (yDistance >= 0) {
			return Math.toDegrees(Math.asin(xDistance/radius));
		} else {
			return Math.toDegrees(90 + Math.asin(yDistance/radius));
		}
	}
}