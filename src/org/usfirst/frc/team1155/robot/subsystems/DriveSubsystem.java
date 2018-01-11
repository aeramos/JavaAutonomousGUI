package org.usfirst.frc.team1155.robot.subsystems;

import com.ctre.phoenix.*;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import org.usfirst.frc.team1155.robot.PortMap;
import org.usfirst.frc.team1155.robot.Robot;

public class DriveSubsystem extends PIDSubsystem {
    public static final int WHEEL_RADIUS = 2;
    private static DoubleSolenoid frontPivots;
    private static DoubleSolenoid backPivots;
    private static DriveMode driveMode;
    public boolean angleDrive = false;
    public SensorMode sensorMode;
    private TalonSRX frontLeftMotor;
    private TalonSRX frontRightMotor;
    private TalonSRX backLeftMotor;
    private TalonSRX backRightMotor;

    public DriveSubsystem() {
        super("Drive", 1.0, 0.1, 0.1);

        frontLeftMotor = new TalonSRX(PortMap.DRIVE_FRONT_LEFT_TALON);
        frontRightMotor = new TalonSRX(PortMap.DRIVE_FRONT_RIGHT_TALON);
        backLeftMotor = new TalonSRX(PortMap.DRIVE_BACK_LEFT_TALON);
        backRightMotor = new TalonSRX(PortMap.DRIVE_BACK_RIGHT_TALON);

        frontPivots = new DoubleSolenoid(PortMap.DRIVE_RIGHT_PISTONS[0], PortMap.DRIVE_RIGHT_PISTONS[1]);
        backPivots = new DoubleSolenoid(PortMap.DRIVE_LEFT_PISTONS[0], PortMap.DRIVE_LEFT_PISTONS[1]);

        driveMode = DriveMode.TANK;

        resetEncoders();

        getPIDController().setContinuous(true);
        getPIDController().setPercentTolerance(20.0);
    }

    // call this normally
    // lateralJoy is rightJoystick
    public void setSpeed(Joystick lateralJoy, Joystick rotationalJoy) {
        if (angleDrive) {
            return;
        }
        switch (driveMode) {
            case TANK:
                setTankSpeed(-rotationalJoy.getY(), -lateralJoy.getY());
                break;
            case MECANUM:
                double yVal = -lateralJoy.getY();
                double xVal = -lateralJoy.getX();
                double rotationalVal = rotationalJoy.getX();
                setMechSpeed(xVal, yVal, rotationalVal);
                break;
        }
    }

    // call this to force tank drive
    public void setTankSpeed(double leftVal, double rightVal) {
        if (angleDrive) {
            return;
        }
        frontRightMotor.set(ControlMode.PercentOutput, rightVal);
        frontLeftMotor.set(ControlMode.PercentOutput, -leftVal);
        backRightMotor.set(ControlMode.PercentOutput, rightVal);
        backLeftMotor.set(ControlMode.PercentOutput, -leftVal);
    }

    // call this to force mecanum drive
    public void setMechSpeed(double xVal, double yVal, double rotationalVal) {
        if (angleDrive) {
            return;
        }
        xVal = -xVal;
        // yVal = -yVal;
        rotationalVal = -rotationalVal;

        frontLeftMotor.set(ControlMode.PercentOutput, -xVal - yVal + rotationalVal);
        frontRightMotor.set(ControlMode.PercentOutput, -xVal + yVal + rotationalVal);
        backLeftMotor.set(ControlMode.PercentOutput, xVal - yVal + rotationalVal);
        backRightMotor.set(ControlMode.PercentOutput, xVal + yVal + rotationalVal);
    }

    public void moveDegrees(double degrees) {
        angleDrive = true;

        double conversionFactor = Math.PI / 180;

        double xVal = -Math.cos(degrees * conversionFactor);
        double yVal = Math.sin(degrees * conversionFactor);

        System.out.println(xVal + " " + yVal + " " + degrees);

        frontLeftMotor.set(ControlMode.PercentOutput, -xVal - yVal);
        frontRightMotor.set(ControlMode.PercentOutput, -xVal + yVal);
        backLeftMotor.set(ControlMode.PercentOutput, xVal - yVal);
        backRightMotor.set(ControlMode.PercentOutput, xVal + yVal);
    }

    public DriveMode getDriveMode() {
        return driveMode;
    }

    public void setDriveMode(DriveMode mode) {
        driveMode = mode;

        switch (mode) {
            case MECANUM:
                engageWheels(Value.kForward);
                break;
            case TANK:
                engageWheels(Value.kReverse);
                break;
        }
    }

    private void engageWheels(DoubleSolenoid.Value value) {
        frontPivots.set(value);
        backPivots.set(value);
    }

    public void resetEncoders() {
        frontLeftMotor.getSensorCollection(). setQuadraturePosition (0, 10);
        frontRightMotor.getSensorCollection(). setQuadraturePosition (0, 10);
    }

    public double getEncDistance() {
        return -frontLeftMotor.getEncPosition() * (1 / 44.2);
    }

    @Override
    public void initDefaultCommand() {
    }

    public void startAdjustment(double current, double setPoint) {
        // Enables PIDController with given setpoint.
        setPoint %= 360;
        // Sets angle to corresponding reference angle.
        setSetpoint((int)(((current - setPoint >= 0 ? 180 : -180) + current - setPoint) / 360) * 360 + setPoint);
        // Rounds difference of current and desired angle to nearest 360
        // degrees, then adds the desired angle to make final set point.
        enable();
    }

    public void endAdjustment() {
        // Disable PID Controller
        getPIDController().disable();
    }

    @Override
    protected double returnPIDInput() {
        // Returns input to be used by the PIDController
        if (sensorMode == SensorMode.GYRO) {
            return Robot.gyro.getAngle();
        } else {
            return getEncDistance();
        }
    }

    @Override
    protected void usePIDOutput(double output) {
        // Set wheels to values given by PIDController
        if (sensorMode == SensorMode.GYRO) {
            output *= 0.5;
            frontLeftMotor.pidWrite(-output);
            backLeftMotor.pidWrite(-output);
            frontRightMotor.pidWrite(-output);
            backRightMotor.pidWrite(-output);
        } else {
            output *= -0.4;
            frontLeftMotor.pidWrite(output);
            backLeftMotor.pidWrite(output);
            frontRightMotor.pidWrite(-output);
            backRightMotor.pidWrite(-output);
        }
    }

    public enum DriveMode {
        MECANUM, TANK, TURN_FRONT, TURN_BACK
    }

    public enum SensorMode {
        ENCODER, GYRO
    }
}
