package org.usfirst.frc.team1155.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1155.robot.commands.TankDriveCommand;
import org.usfirst.frc.team1155.robot.subsystems.ClimberSubsystem;
import org.usfirst.frc.team1155.robot.subsystems.DriveSubsystem;
import org.usfirst.frc.team1155.robot.subsystems.GearSubsystem;
import org.usfirst.frc.team1155.robot.subsystems.ShooterSubsystem;

public class Robot extends IterativeRobot {
    public static DriveSubsystem driveSubsystem;
    public static ShooterSubsystem shooterSubsystem;
    public static GearSubsystem gearSubsystem;
    public static ClimberSubsystem climberSubsystem;
    public static ADXRS450_Gyro gyro;
    public static RioDuinoController rioDuino;
    public static DriverStation.Alliance allianceColor;
    public static boolean isInTeleop;
    private static SmartDashboard smart;
    private static OI oi;
    private static DesCartesianPlane plane;
    private static ADXL345_I2C accelerometer;
    private static Compressor compressor;
    private Timer timer;

    @Override
    public void robotInit() {
        timer = new Timer();
        gyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
        accelerometer = new ADXL345_I2C(PortMap.ACCEL_PORT, PortMap.ACCEL_RANGE);
        smart = new SmartDashboard();

        plane = new DesCartesianPlane(timer, gyro, accelerometer);

        driveSubsystem = new DriveSubsystem();
        gearSubsystem = new GearSubsystem();
        shooterSubsystem = new ShooterSubsystem();
        climberSubsystem = new ClimberSubsystem();

        compressor = new Compressor(0);

        oi = new OI();

        rioDuino = new RioDuinoController();

        isInTeleop = false;

        SmartDashboard.putString("Auto Routine: ", "ACTION POSITION");
    }

    @Override
    public void disabledInit() {
        Robot.driveSubsystem.endAdjustment();

        if (shooterSubsystem.leftAgitatorServo != null && shooterSubsystem.rightAgitatorServo != null) {
            shooterSubsystem.stopAgitators();
        }
    }

    @Override
    public void teleopInit() {
        plane.timer.setStartTime();
        driveSubsystem.resetEncoders();
        driveSubsystem.endAdjustment();

        isInTeleop = true;

        new TankDriveCommand().start();
    }

    @Override
    public void testInit() {
        compressor = new Compressor(0);
        compressor.start();
    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();

        //testing servos
        Robot.shooterSubsystem.leftShootServo.set(-OI.leftJoystick.getThrottle());
        Robot.shooterSubsystem.rightShootServo.set(1 + OI.leftJoystick.getThrottle());

        if (OI.leftJoystick.getRawButton(1) || OI.rightJoystick.getRawButton(1)) {
            compressor.stop();
        } else if (!compressor.enabled()) {
            compressor.start();
        }

        plane.updatePosition();

        SmartDashboard.putNumber("DB/getX", plane.getX());
        SmartDashboard.putNumber("DB/getY", plane.getY());
    }

    @Override
    public void testPeriodic() {
        LiveWindow.run();
    }
}
