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
				if (Robot.René.getX() > 10.00) {
					frontLeftMotor.set(-1.0);
					frontRightMotor.set(-1.0);
					backLeftMotor.set(-1.0);
					backRightMotor.set(-1.0);
				} else if (Robot.René.getX() < 10.00) {
					frontLeftMotor.set(1.0);
					frontRightMotor.set(1.0);
					backLeftMotor.set(1.0);
					backRightMotor.set(1.0);
				} else {
					//Does nothing as it would be in the same place
				}
				if (Robot.René.getY() > 10.00) {
					frontLeftMotor.set(-1.0);
					frontRightMotor.set(-1.0);
					backLeftMotor.set(-1.0);
					backRightMotor.set(-1.0);
				} else if (Robot.René.getY() < 10.00) {
					frontLeftMotor.set(1.0);
					frontRightMotor.set(1.0);
					backLeftMotor.set(1.0);
					backRightMotor.set(1.0);
				} else {
					//Does Nothing as it would be in the same place
				}
			}
		} else if (location == Location.Airship) {
			while (Robot.René.getX() != 5.00 && Robot.René.getY() != 5.00) {
				if (Robot.René.getX() > 5.00) {
					frontLeftMotor.set(-1.0);
					frontRightMotor.set(-1.0);
					backLeftMotor.set(-1.0);
					backRightMotor.set(-1.0);
				} else if (Robot.René.getX() < 10.00) {
					frontLeftMotor.set(1.0);
					frontRightMotor.set(1.0);
					backLeftMotor.set(1.0);
					backRightMotor.set(1.0);
				} else {
					//Does nothing as it would be in the same place
				}
				if (Robot.René.getY() > 5.00) {
					frontLeftMotor.set(-1.0);
					frontRightMotor.set(-1.0);
					backLeftMotor.set(-1.0);
					backRightMotor.set(-1.0);
				} else if (Robot.René.getY() < 10.00) {
					frontLeftMotor.set(1.0);
					frontRightMotor.set(1.0);
					backLeftMotor.set(1.0);
					backRightMotor.set(1.0);
				} else {
					//Does Nothing as it would be in the same place
				}	
			}
		} else if (location == Location.StartingPos) {
			while (Robot.René.getX() != 0.00 && Robot.René.getY() != 0.00) {
				if (Robot.René.getX() > 0.00) {
					frontLeftMotor.set(-1.0);
					frontRightMotor.set(-1.0);
					backLeftMotor.set(-1.0);
					backRightMotor.set(-1.0);
				} else if (Robot.René.getX() < 0.00) {
					frontLeftMotor.set(1.0);
					frontRightMotor.set(1.0);
					backLeftMotor.set(1.0);
					backRightMotor.set(1.0);
				} else {
					//Does nothing as it would be in the same place
				}
				if (Robot.René.getY() > 0.00) {
					frontLeftMotor.set(-1.0);
					frontRightMotor.set(-1.0);
					backLeftMotor.set(-1.0);
					backRightMotor.set(-1.0);
				} else if (Robot.René.getY() < 0.00) {
					frontLeftMotor.set(1.0);
					frontRightMotor.set(1.0);
					backLeftMotor.set(1.0);
					backRightMotor.set(1.0);
				} else {
					//Does Nothing as it would be in the same place
				}				
			}
		} else {
			while (Robot.René.getX() != 0.00 && Robot.René.getY() != 0.00) {
				if (Robot.René.getX() > 0.00) {
					frontLeftMotor.set(-1.0);
					frontRightMotor.set(-1.0);
					backLeftMotor.set(-1.0);
					backRightMotor.set(-1.0);
				} else if (Robot.René.getX() < 0.00) {
					frontLeftMotor.set(1.0);
					frontRightMotor.set(1.0);
					backLeftMotor.set(1.0);
					backRightMotor.set(1.0);
				} else {
					//Does nothing as it would be in the same place
				}
				if (Robot.René.getY() > 0.00) {
					frontLeftMotor.set(-1.0);
					frontRightMotor.set(-1.0);
					backLeftMotor.set(-1.0);
					backRightMotor.set(-1.0);
				} else if (Robot.René.getY() < 0.00) {
					frontLeftMotor.set(1.0);
					frontRightMotor.set(1.0);
					backLeftMotor.set(1.0);
					backRightMotor.set(1.0);
				} else {
					//Does Nothing as it would be in the same place
				}	
			}
		}
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

}
