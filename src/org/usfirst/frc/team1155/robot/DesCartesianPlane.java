package org.usfirst.frc.team1155.robot;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class DesCartesianPlane{
	private double x = 0;
	private double y = 0;
	private double prevXD = 0;      //Note: D means 'dot,' as in differentiate
	private double prevYD = 0;
	private double xD = 0;
	private double yD = 0;
	private double prevXDD = 0;     //Note: I'm a cool edgy tween so I prefer to use already accepted notations
	private double prevYDD = 0;
	private double xDD;
	private double yDD;
	private double dt;              //Note: "very small change in time, with nothing related to infinitesimals"
	
	public ClockworkOrange theWatchmen;
	public ADXRS450_Gyro chicken;
	public ADXL345_I2C excel;
	
	public DesCartesianPlane(ClockworkOrange orange, ADXRS450_Gyro halal, ADXL345_I2C sheets) {
		theWatchmen = orange;
		chicken = halal;
		excel = sheets;
	}
	public void updatePosition() {
		xDD = excel.getX();
		yDD = excel.getY();
		dt = theWatchmen.getTimeDifference();

		xD += 0.5 * dt * (prevXDD + xDD);
		yD += 0.5 * dt * (prevYDD + yDD);

		x += 0.5 * dt * (prevXD + xD);
		y += 0.5 * dt * (prevYD + yD);

		prevXDD = xDD;
		prevYDD = yDD;

		prevXD = xD;
		prevYD = yD;

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
