package org.usfirst.frc.team1155.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team1155.robot.PortMap;
import org.usfirst.frc.team1155.robot.Robot;
import com.ctre.CANTalon;

public class GlobalPositioningSubsystem extends Subsystem{

	public enum Location {
		Gear, Airship, StartingPos;
	}
	
	public static Location place;
	public CANTalon frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;
	
	public GlobalPositioningSubsystem() {
		frontLeftMotor = new CANTalon(PortMap.FRONT_LEFT_TALON);
		frontRightMotor = new CANTalon(PortMap.FRONT_RIGHT_TALON);
		backLeftMotor = new CANTalon(PortMap.BACK_LEFT_TALON);
		backRightMotor = new CANTalon(PortMap.BACK_RIGHT_TALON);		
		place = Location.StartingPos;
	}
	
	public void setLocation(Location location) {
		place = location;
	}
	
	public Location getLocation() {
		return place;
	}
	
	public void goSomewhere(Location location) {
		if (location == Location.Gear) {
			while (Robot.René.getX() != 10.00 && Robot.René.getY() != 10.00) {
				if (Robot.René.angleRequiredToTurn(10.00-Robot.René.getX(), 10.00-Robot.René.getY(), Robot.René.distanceBetweenPoints(Robot.René.getX(), Robot.René.getY(), 10.0, 10.0)) != Robot.René.chicken.getAngle()) {
					if (Robot.René.angleRequiredToTurn(10.00-Robot.René.getX(), 10.00-Robot.René.getY(), Robot.René.distanceBetweenPoints(Robot.René.getX(),Robot.René.getY(), 10.0, 10.0)) >= 180) {
						while (180 - Robot.René.angleRequiredToTurn(10.00-Robot.René.getX(), 10.00-Robot.René.getY(), Robot.René.distanceBetweenPoints(Robot.René.getX(),Robot.René.getY(), 10.0, 10.0)) != Robot.René.chicken.getAngle()) {
							frontRightMotor.set(1);
							backRightMotor.set(1);
						}
					} else {
						while (Robot.René.angleRequiredToTurn(10.00-Robot.René.getX(), 10.00-Robot.René.getY(), Robot.René.distanceBetweenPoints(Robot.René.getX(),Robot.René.getY(), 10.0, 10.0)) != Robot.René.chicken.getAngle()) {
							frontLeftMotor.set(1);
							backLeftMotor.set(1);
					} 
				}
			}
		} 
	}
		else if (location == Location.Airship) {
			while (Robot.René.getX() != 5.00 && Robot.René.getY() != 5.00) {
			
			}
		} else if (location == Location.StartingPos) {
			while (Robot.René.getX() != 0.00 && Robot.René.getY() != 0.00) {				
			
			}
		} else {
			while (Robot.René.getX() != 0.00 && Robot.René.getY() != 0.00) {
			
			}
		}
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

}
