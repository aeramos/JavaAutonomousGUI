package org.usfirst.frc.team1155.robot;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class DesCartesianPlane{
	private double x = 0;
	private double y = 0;
	private double xVelocity = 0;
	private double yVelocity = 0;
	private double preVelocityX = 0;
	private double preVelocityY = 0;
	private double prevAccelX = 0;
	private double prevAccelY = 0;
	private double accelX;
	private double accelY;
	
	public ClockworkOrange theWatchmen;
	public ADXRS450_Gyro chicken;
	public ADXL345_I2C excel;
	
	public DesCartesianPlane(ClockworkOrange orange, ADXRS450_Gyro halal, ADXL345_I2C sheets) {
		theWatchmen = orange;
		chicken = halal;
		excel = sheets;
	}
	public void updatePosition() {
		currentXaccel = excel.getX();
		currentYaccel = excel.getY();

		

		for(double i = theWatchmen.getPreviousTime(); i <= theWatchmen.getCurrentTime(); i += theWatchmen.getTimeDifference()){
			xVelocity += 0.5 * i * (prevAccelX + currentXaccel);
			yVelocity += 0.5 * i * (prevAccelY + currentYaccel);
			prevAccelX = currentXaccel;
			prevAccelY = currentYaccel;
		}
		for(double i = theWatchmen.getPreviousTime(); i <= theWatchmen.getCurrentTime(); i += theWatchmen.getTimeDifference()){
			x += 0.5 * i * (preVelocityX + xVelocity);
			y += 0.5 * i * (preVelocityY + yVelocity);
			preVelocityX = xVelocity;
			preVelocityY = yVelocity;
		}
	}
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
		
	
	
	public double getVelocity() {
		return velocity; //Shouldn't be needed, mostly for testing
	}
	
	public void setVelocity(double set) {	
		velocity = set; //Shouldn't be needed, mostly for testing
	}
	
	public double distanceBetweenPoints(double x1, double y1, double x2, double y2) {
		return Math.hypot(x2 - x1, y2 - y1);//bowen is sus
	}
	
	public double distanceBetweenCoords(double x1y1, double x2y2) {
		return x2y2 - x1y1;
	}

	public double angleRequiredToTurn(double xDistance, double yDistance, double radius) { //Basically Bowen
		if (yDistance >= 0) {
			return Math.toDegrees(Math.asin(xDistance/radius));
		} else {
			return 90 + Math.toDegrees(Math.asin(yDistance/radius));
		}
	}
	
	public double getUsefulAngle() {
		if (chicken.getAngle() > 180) {
			return -(360 - chicken.getAngle());
		} else {
			return chicken.getAngle();
		}
	}
}
