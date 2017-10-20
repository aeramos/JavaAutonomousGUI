package org.usfirst.frc.team1155.robot;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import flanagan.integration.Integration;
import flanagan.integration.IntegralFunction;

public class DesCartesianPlane{
	private double x;
	private double y;
	
	ClockworkOrange theWatchmen;
	
	public DesCartesianPlane(ClockworkOrange orange){
		theWatchmen = orange;
	}
	
}