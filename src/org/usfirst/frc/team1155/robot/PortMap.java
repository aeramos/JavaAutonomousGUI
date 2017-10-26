package org.usfirst.frc.team1155.robot;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;

public class PortMap {
	
	//**********JOYSTICKS**********//

	public static final int JOYSTICK_LEFT = 0;
	public static final int JOYSTICK_RIGHT = 1;
	
	//************DRIVE************//
	
	public static final int FRONT_LEFT_TALON = 0;
	public static final int FRONT_RIGHT_TALON = 1;
	public static final int BACK_LEFT_TALON = 2;
	public static final int BACK_RIGHT_TALON = 3;
	
	//***********SENSORS***********//
	
	public static final SPI.Port GYRO_PORT = SPI.Port.kMXP;
	public static final I2C.Port ACCEL_PORT = I2C.Port.kOnboard;
	public static final Accelerometer.Range ACCEL_RANGE = Accelerometer.Range.k16G;

}
