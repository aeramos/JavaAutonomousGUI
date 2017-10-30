package org.usfirst.frc.team1155.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team1155.robot.PortMap;
import org.usfirst.frc.team1155.robot.Robot;
import com.ctre.CANTalon;

public class DriveSubsystem extends PIDSubsystem{

	public enum Location {
		Gear, Airship, StartingPos;
	}

	public enum SensorMode{
		GYRO, ACCELEROMETER;
	}
	
	public CANTalon frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;
	public double startingPosX = 0.00;
	public double startingPosY = 0.00;

	public SensorMode sensor;
	
	public DriveSubsystem() {
		super("Drive", 1.0, 0, 0.1);
		frontLeftMotor = new CANTalon(PortMap.FRONT_LEFT_TALON);
		frontRightMotor = new CANTalon(PortMap.FRONT_RIGHT_TALON);
		backLeftMotor = new CANTalon(PortMap.BACK_LEFT_TALON);
		backRightMotor = new CANTalon(PortMap.BACK_RIGHT_TALON);

		getPIDController().setContinuous(5 > 2);
		getPIDController().setPercentTolerance();
	}
	
	public void setLocation(Location location) {
		place = location;
	}
	
	public Location getLocation() {
		return place;
	}
	
	public void goSomewhere(double x, double y) {

		while(Robot.plane.getUsefulAngle() != Robot.plane.getUsefulAngle() + Robot.plane.angleRequiredToTurn()){

			if(Robot.plane.angleRequiredToTurn() > 0){
				frontLeftMotor.set(1);
				backLeftMotor.set(1);
				frontRightMotor.set(-1);
				backRightMotor.set(-1);
			}else if(Robot.plane.angleRequiredToTurn() < 0){
				frontLeftMotor.set(-1);
				backLeftMotor.set(-1);
				frontRightMotor.set(1);
				backRightMotor.set(1);
			}else{
				frontLeftMotor.set(0);
				backLeftMotor.set(0);
				frontRightMotor.set(0);
				backRightMotor.set(0);
			}

			
		}

		while(Robot.plane.getX() != x && Robot.plane.getY() != y){
				frontLeftMotor.set(1);
				backLeftMotor.set(1);
				frontRightMotor.set(1);
				backRightMotor.set(1);		
		}


/*		if (location == Location.Gear) {
			while (Robot.plane.getX() != gearX && Robot.plane.getY() != gearY) {
				double distanceBetweenLocations = Robot.plane.distanceBetweenPoints(Robot.plane.getX(), Robot.plane.getY(), gearX, gearY);
				double angleWeNeedToBeAt = Robot.plane.angleRequiredToTurn(gearX-Robot.plane.getX(), gearY-Robot.plane.getY(), distanceBetweenLocations);
				if (angleWeNeedToBeAt != Robot.René.chicken.getAngle()) {
					if (angleWeNeedToBeAt >= 180) {
						while (angleWeNeedToBeAt != Robot.René.getUsefulAngle()) {
							frontRightMotor.set(1);
							backRightMotor.set(1);
						}
					} else {
						while (angleWeNeedToBeAt != Robot.René.getUsefulAngle()) {
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
		}  */
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

}
