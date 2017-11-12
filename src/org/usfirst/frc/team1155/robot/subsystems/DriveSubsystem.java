package org.usfirst.frc.team1155.robot.subsystems;

import com.ctre.CANTalon;
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
    private CANTalon frontLeftMotor;
    private CANTalon frontRightMotor;
    private CANTalon backLeftMotor;
    private CANTalon backRightMotor;

    public DriveSubsystem() {
        super("Drive", 1.0, 0.1, 0.1);

        frontLeftMotor = new CANTalon(PortMap.DRIVE_FRONT_LEFT_TALON);
        frontRightMotor = new CANTalon(PortMap.DRIVE_FRONT_RIGHT_TALON);
        backLeftMotor = new CANTalon(PortMap.DRIVE_BACK_LEFT_TALON);
        backRightMotor = new CANTalon(PortMap.DRIVE_BACK_RIGHT_TALON);

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
        frontRightMotor.set(rightVal);
        frontLeftMotor.set(-leftVal);
        backRightMotor.set(rightVal);
        backLeftMotor.set(-leftVal);
    }

    // call this to force mecanum drive
    public void setMechSpeed(double xVal, double yVal, double rotationalVal) {
        if (angleDrive) {
            return;
        }
        xVal = -xVal;
        // yVal = -yVal;
        rotationalVal = -rotationalVal;

        frontLeftMotor.set(-xVal - yVal + rotationalVal);
        frontRightMotor.set(-xVal + yVal + rotationalVal);
        backLeftMotor.set(xVal - yVal + rotationalVal);
        backRightMotor.set(xVal + yVal + rotationalVal);
    }

    public void moveDegrees(double degrees) {
        angleDrive = true;

        double conversionFactor = Math.PI / 180;

        double xVal = -Math.cos(degrees * conversionFactor);
        double yVal = Math.sin(degrees * conversionFactor);

        System.out.println(xVal + " " + yVal + " " + degrees);

        frontLeftMotor.set(-xVal - yVal);
        frontRightMotor.set(-xVal + yVal);
        backLeftMotor.set(xVal - yVal);
        backRightMotor.set(xVal + yVal);
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
        frontLeftMotor.setEncPosition(0);
        frontRightMotor.setEncPosition(0);
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
