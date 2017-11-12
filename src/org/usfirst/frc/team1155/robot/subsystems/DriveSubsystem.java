package org.usfirst.frc.team1155.robot.subsystems;

import org.usfirst.frc.team1155.robot.PortMap;
import org.usfirst.frc.team1155.robot.Robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class DriveSubsystem extends PIDSubsystem {

	public enum DriveMode {
		MECHANUM, TANK, TURN_FRONT, TURN_BACK;
	}
	
	public enum SensorMode {
		ENCODER, GYRO;
	}

	public CANTalon frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;
	public static DoubleSolenoid frontPivots, backPivots;

	public static DriveMode driveMode;
	public static final int WHEEL_RADIUS = 2;
	
	public boolean angleDrive = false;
	
	public SensorMode sensorMode;

	public Ultrasonic ultrasonic;
	
	public DriveSubsystem() {
		super("Drive", 1.0, 0.1, 0.1);

		frontLeftMotor = new CANTalon(PortMap.DRIVE_FRONT_LEFT_TALON);
		frontRightMotor = new CANTalon(PortMap.DRIVE_FRONT_RIGHT_TALON);
		backLeftMotor = new CANTalon(PortMap.DRIVE_BACK_LEFT_TALON);
		backRightMotor = new CANTalon(PortMap.DRIVE_BACK_RIGHT_TALON);

		frontPivots = new DoubleSolenoid(PortMap.DRIVE_RIGHT_PISTONS[0], PortMap.DRIVE_RIGHT_PISTONS[1]);
		backPivots = new DoubleSolenoid(PortMap.DRIVE_LEFT_PISTONS[0], PortMap.DRIVE_LEFT_PISTONS[1]);
		
		driveMode = DriveMode.TANK;
		
		//ultrasonic = new Ultrasonic(PortMap.ULTRASONIC[0], PortMap.ULTRASONIC[1]);
		//ultrasonic.setEnabled(true);
		
		
    	resetEncoders();

		getPIDController().setContinuous(true);
		getPIDController().setPercentTolerance(20.0);
	}

	// call this normally
	// lateralJoy is rightJoystick
	public void setSpeed(Joystick lateralJoy, Joystick rotationalJoy) {
		if(angleDrive) {
			return;
		}
		switch (driveMode) {
		case TANK:
			setTankSpeed(-rotationalJoy.getY(), -lateralJoy.getY());
			break;
		case MECHANUM:
			double yVal = -lateralJoy.getY();
			double xVal = -lateralJoy.getX();
			double rotationalVal = rotationalJoy.getX();
			setMechSpeed(xVal, yVal, rotationalVal);
			break;
		/*default:
			SmartDashboard.putString("Drive Error", "No DriveMode!!!");*/
		}
	}

	// call this to force tank drive
	public void setTankSpeed(double leftVal, double rightVal) {
		if(angleDrive) {
			return;
		}
		frontRightMotor.set(rightVal);
		frontLeftMotor.set(-leftVal);
		backRightMotor.set(rightVal);
		backLeftMotor.set(-leftVal);
	}

	// call this to force mechanum drive
	public void setMechSpeed(double xVal, double yVal, double rotationalVal) {
		if(angleDrive) {
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
			
		double conversionFactor = Math.PI/180;
		
		double xVal = -Math.cos(degrees * conversionFactor);
		double yVal = Math.sin(degrees * conversionFactor);
		
		System.out.println(xVal + " " + yVal + " " + degrees);
		
		frontLeftMotor.set(-xVal - yVal);
		frontRightMotor.set(-xVal + yVal);
		backLeftMotor.set(xVal - yVal);
		backRightMotor.set(xVal + yVal);
	}

	public void setDriveMode(DriveMode mode) {
		//SmartDashboard.putString("DriveMode", mode.name());
		driveMode = mode;

		switch (mode) {
		case MECHANUM:
			engageWheels(Value.kForward);
			break;
		case TANK:
			engageWheels(Value.kReverse);
			break;
		}
	}

	public DriveMode getDriveMode() {
		return driveMode;
	}

	public void engageWheels(DoubleSolenoid.Value value) {
//		if (value == Value.kForward){
//			frontPivots.set(Value.kForward);
//			backPivots.set(Value.kReverse);
//		}
//		else{
//			frontPivots.set(Value.kReverse);
//			backPivots.set(Value.kForward);
//		}
		frontPivots.set(value);
		backPivots.set(value);
		//SmartDashboard.putString("Wheels", value.name());
	}
	
    public void resetEncoders(){
    	frontLeftMotor.setEncPosition(0);
    	frontRightMotor.setEncPosition(0);
    }
    
    public double getEncDistance(){
    	return -frontLeftMotor.getEncPosition() * (1/44.2);
    }

	@Override
	public void initDefaultCommand() {

	}

	public void startAdjustment(double current, double setPoint) {
		// Enables PIDController with given setpoint.
		setPoint %= 360;
		// Sets angle to corresponding reference angle.
		setSetpoint((int) (((current - setPoint >= 0 ? 180 : -180) + current - setPoint) / 360) * 360 + setPoint);
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
		if (sensorMode == SensorMode.GYRO)
			return Robot.gyro.getAngle();
		return getEncDistance();
	}

	@Override
	protected void usePIDOutput(double output) {
		// Set wheels to values given by PIDController
		
		if(sensorMode == SensorMode.GYRO) {
			output *= 0.5;
			frontLeftMotor.pidWrite(-output);
			backLeftMotor.pidWrite(-output);
			frontRightMotor.pidWrite(-output);
			backRightMotor.pidWrite(-output);
		}else {
			output *= -0.4;
			frontLeftMotor.pidWrite(output);
			backLeftMotor.pidWrite(output);
			frontRightMotor.pidWrite(-output);
			backRightMotor.pidWrite(-output);
		}

	}
	
//	public double getUltrasonic() {
//		return ultrasonic.getRangeInches();
//	}

}
