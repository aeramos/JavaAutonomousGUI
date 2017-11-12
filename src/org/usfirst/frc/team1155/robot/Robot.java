
package org.usfirst.frc.team1155.robot;


import org.usfirst.frc.team1155.robot.commands.AutonomousCommand;
import org.usfirst.frc.team1155.robot.commands.AutonomousCommand.AutoRoutine;
import org.usfirst.frc.team1155.robot.commands.TankDriveCommand;
import org.usfirst.frc.team1155.robot.subsystems.ClimberSubsystem;
import org.usfirst.frc.team1155.robot.subsystems.DriveSubsystem;
import org.usfirst.frc.team1155.robot.subsystems.GearSubsystem;
import org.usfirst.frc.team1155.robot.subsystems.ImageSubsystem;
import org.usfirst.frc.team1155.robot.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.ADXL345_SPI;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;


public class Robot extends IterativeRobot {
	int x = 0;
	public static DriveSubsystem driveSubsystem; 
	public static ShooterSubsystem shooterSubsystem;
	public static GearSubsystem gearSubsystem;
	public static ClimberSubsystem climberSubsystem;
//	public static ImageSubsystem imageSubsystem;
	public static SmartDashboard smart;
	public static OI oi;


	public static DesCartesianPlane plane;
	
	public Timer timer;
//	public static ADXL345_I2C accelerometer;
	public static ADXL345_SPI accel;
	public static ADXRS450_Gyro gyro;
	
	public static AutoRoutine autoRoutine;
	
	public static RioDuinoController rioDuino;
	public static DriverStation.Alliance allianceColor;
	String rioDuinoLEDMode;
	public static Compressor compressor;

	public static boolean isInTeleop;
	
	@Override
	public void robotInit() {

		timer = new Timer();
    	gyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
    	//accelerometer = new ADXL345_I2C(PortMap.ACCEL_PORT, PortMap.ACCEL_RANGE);
    	accel = new ADXL345_SPI();
    	smart = new SmartDashboard();
    	
    	plane = new DesCartesianPlane(timer, gyro, accel);

		driveSubsystem = new DriveSubsystem();
		gearSubsystem = new GearSubsystem();
		shooterSubsystem = new ShooterSubsystem();
		climberSubsystem = new ClimberSubsystem();
//		imageSubsystem = new ImageSubsystem();
		
		compressor = new Compressor(0);
		
		oi = new OI();
		
		rioDuino = new RioDuinoController();
		
		isInTeleop = false;
		
		SmartDashboard.putString("Auto Routine: ", "ACTION POSITION");
	}
	
	@Override
	public void teleopInit() {
		//gyro.reset();
		plane.timer.setStartTime();
		driveSubsystem.resetEncoders();
		driveSubsystem.endAdjustment();
		
		isInTeleop = true;
		
		//rioDuino.SendString("green");

		new TankDriveCommand().start(); 
//		compressor = new Compressor(0);
	}

	@Override
	public void teleopPeriodic() {
    	Scheduler.getInstance().run();

    	//SmartDashboard.putNumber("Gyro Angle", Robot.gyro.getAngle());
    	//SmartDashboard.putNumber("Ultrasonic Distance", Robot.driveSubsystem.getUltrasonic());
    	//SmartDashboard.putString("Ultrasonic Valid", "" + Robot.driveSubsystem.ultrasonic.isRangeValid());
    	//System.out.println(Robot.driveSubsystem.getEncDistance());
    	  
    	//testing servos
    	Robot.shooterSubsystem.leftShootServo.set(-OI.leftJoystick.getThrottle());
    	Robot.shooterSubsystem.rightShootServo.set(1+OI.leftJoystick.getThrottle());
    	//System.out.println(Robot.shooterSubsystem.leftShootServo.get());
		
		if (OI.leftJoystick.getRawButton(1) || OI.rightJoystick.getRawButton(1)){
			compressor.stop();
		}
		else if(!compressor.enabled()){
			compressor.start();
		}
		
		plane.updatePosition();
		
		smart.putNumber("DB/getX", plane.getX());
		smart.putNumber("DB/getY", plane.getY());
	}
	
	/*	@Override
	public void autonomousInit() {
		isInTeleop = false;
		
		switch(SmartDashboard.getString("Auto Routine: ").toLowerCase()){
		case "gear left":
			autoRoutine = AutoRoutine.GEAR_LEFT;
			break;
		case "gear right":
			autoRoutine = AutoRoutine.GEAR_RIGHT;
			break;
		case "gear middle":
			autoRoutine = AutoRoutine.GEAR_MIDDLE;
			break;
		case "shoot red":
			autoRoutine = AutoRoutine.SHOOT_RED;
			break;
		case "shoot blue":
			autoRoutine = AutoRoutine.SHOOT_BLUE;
			break;
		default:
			autoRoutine = AutoRoutine.NOTHING;
			break;
		}
		
		//compressor.start();
		autoRoutine = AutoRoutine.GEAR_MIDDLE;
		//System.out.println(autoRoutine.name());
		new AutonomousCommand(autoRoutine).start();	
		
	}
*/
	@Override
	public void autonomousPeriodic() {
    	//SmartDashboard.putNumber("Gyro Angle", Robot.gyro.getAngle());

		Scheduler.getInstance().run();
	}

	@Override
	public void disabledInit() {	
		Robot.driveSubsystem.endAdjustment();
		
		if(shooterSubsystem.leftAgitatorServo != null && shooterSubsystem.rightAgitatorServo != null) {
			shooterSubsystem.stopAgitators();
		}
		
//		if(rioDuino != null)
//			rioDuino.SendString("disableInit");
		
//		if(gyro != null)
//			gyro.reset();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void testInit() {
		compressor = new Compressor(0);
		compressor.start();
	}
	
	@Override
	public void testPeriodic() {
		LiveWindow.run();

	}
}
