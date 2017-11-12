package org.usfirst.frc.team1155.robot;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;

public class PortMap {
		
	//****************JOYSTICKS*******************//

	public static final int JOYSTICK_LEFT = 0;
	public static final int JOYSTICK_RIGHT = 1;
	
	//****************DRIVE*******************//
	
	public static final int DRIVE_FRONT_LEFT_TALON = 7;
	public static final int DRIVE_FRONT_RIGHT_TALON = 5;
	public static final int DRIVE_BACK_RIGHT_TALON = 2;
	public static final int DRIVE_BACK_LEFT_TALON = 6;
	
	public static final int[] DRIVE_RIGHT_PISTONS = {2, 5};
	public static final int[] DRIVE_LEFT_PISTONS = {0, 7};
	
	public static final int[] ULTRASONIC = {1,0};
		
	//****************GEAR*******************//
	
	public static final int[] GEAR_PISTONS = {4,6};

	//****************SHOOT*******************//

	public static final int RIGHT_SHOOT_TALON = 1;
	public static final int LEFT_SHOOT_TALON = 0;
	
	public static final int LEFT_SHOOT_SERVO = 2; //2
	public static final int RIGHT_SHOOT_SERVO = 1; //1
	
	public static final int LEFT_AGITATOR_SERVO = 3; //3
	public static final int RIGHT_AGITATOR_SERVO = 0; //0
	
	//****************CLIMBER*******************//

	public static final int LEFT_CLIMBER_TALON = 3;
	public static final int RIGHT_CLIMBER_TALON = 4;
	
	//***********SENSORS***********//
	
	public static final I2C.Port ACCEL_PORT = I2C.Port.kOnboard;
	public static final Accelerometer.Range ACCEL_RANGE = Accelerometer.Range.k16G;
	
}
