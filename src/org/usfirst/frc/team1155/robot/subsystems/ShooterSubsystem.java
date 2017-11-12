package org.usfirst.frc.team1155.robot.subsystems;

import org.usfirst.frc.team1155.robot.OI;
import org.usfirst.frc.team1155.robot.PortMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */

public class ShooterSubsystem extends Subsystem {

	public Servo leftShootServo, leftAgitatorServo;
	public Servo rightShootServo, rightAgitatorServo;
	
	public CANTalon leftShootTalon, rightShootTalon;
			
	public enum ServoPosition {
		POSITION_1 (90/180), POSITION_2 (100/180);

		private final double value;
		
		ServoPosition(double value) {
			this.value = value;
		}
	}
	
	public ShooterSubsystem() {
				
		//****************LEFT SIDE*******************//

		leftShootTalon = new CANTalon(PortMap.LEFT_SHOOT_TALON);
		leftShootTalon.set(0);
		
		leftShootServo = new Servo(PortMap.LEFT_SHOOT_SERVO);
		
		leftAgitatorServo = new Servo(PortMap.LEFT_AGITATOR_SERVO);
		
		//****************RIGHT SIDE*******************//
		
		rightShootTalon = new CANTalon(PortMap.RIGHT_SHOOT_TALON);
		rightShootTalon.set(0);
		
		rightShootServo = new Servo(PortMap.RIGHT_SHOOT_SERVO);
		
		rightAgitatorServo = new Servo(PortMap.RIGHT_AGITATOR_SERVO);
		
		setServoPosition(ServoPosition.POSITION_1);
		stopAgitators();
		System.out.println("started");
	}
	
	public void setCorrectServoPosition() {
		//logic for correct position goes here
		//setServoPosition(correctPosition);
		
	}
	
	public void setServoPosition(ServoPosition servoPos) {
		leftShootServo.setPosition(servoPos.value);
		rightShootServo.setPosition(1 - servoPos.value);
	}

	public void setLeftShooter(double shootSpeed, int agitateSpeed) {
		leftShootTalon.set(-0.53);
		
		//int convertedToRawSpeed = getRawSpeed(agitateSpeed);
		leftAgitatorServo.setRaw(1700);
	}
	
	public void setRightShooter(double shootSpeed, int agitateSpeed) {
		rightShootTalon.set(0.53);
		//System.out.println(rightShootServo.get());
		//int convertedToRawSpeed = getRawSpeed(agitateSpeed);
		rightAgitatorServo.setRaw(300);
	}
	
	public void stopAgitators() {
		leftAgitatorServo.setRaw(1000);
		rightAgitatorServo.setRaw(1000);
	}
	
	public int getRawSpeed(double value){
		return (int) (1000 + (700 * value)); //max 1700, min 300
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

