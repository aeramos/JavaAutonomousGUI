package org.usfirst.frc.team1155.robot.subsystems;

import org.usfirst.frc.team1155.robot.PortMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class GearSubsystem extends Subsystem {
	
	public enum GearPosition {
		OPEN, CLOSE;
	}
	
	public DoubleSolenoid gearPistons;
	
//	public static Ultrasonic ultrasonic;
	
	//Min and max distances for the wall and the gear when it is in the robot 
	public static final double FARTHEST_FROM_WALL = 0.0;
	public static final double CLOSEST_TO_WALL = 0.0;
	public static final double GEAR_CLOSEST = 0.0;
	public static final double GEAR_FARTHEST = 0.0;
	
	public GearSubsystem() {
		gearPistons = new DoubleSolenoid(0, PortMap.GEAR_PISTONS[0], PortMap.GEAR_PISTONS[1]);
		gearPistons.set(DoubleSolenoid.Value.kForward);
	}
	
	public void setGearPosition(GearPosition position) {
		if(position == GearPosition.OPEN) {
			gearPistons.set(DoubleSolenoid.Value.kReverse);
		}else {
			gearPistons.set(DoubleSolenoid.Value.kForward);
		}
		//SmartDashboard.putString("Gear Position", gearPistons.get().name());
	}

	public void enableUltrasonic() {
		//ultrasonic.setEnabled(true);
	}
	
	//Checks whether the robot is in a suitable distance from the wall
	public boolean validDistFromWall(){
		//return (ultrasonic.getRangeInches() <= FARTHEST_FROM_WALL && ultrasonic.getRangeInches() >= CLOSEST_TO_WALL);
		return false;
	}  
	
	//Uses ultrasonic to detect wether the gear is in the robot
	public boolean hasGear(){
		return false;
		//return (ultrasonic.getRangeInches() <= GEAR_FARTHEST && ultrasonic.getRangeInches() >= GEAR_CLOSEST);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

