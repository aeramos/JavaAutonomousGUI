package org.usfirst.frc.team1155.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team1155.robot.PortMap;

public class GearSubsystem extends Subsystem {
    //Min and max distances for the wall and the gear when it is in the robot
    public static final double FARTHEST_FROM_WALL = 0.0;
    public static final double CLOSEST_TO_WALL = 0.0;
    public static final double GEAR_CLOSEST = 0.0;
    public static final double GEAR_FARTHEST = 0.0;
    private DoubleSolenoid gearPistons;

    public GearSubsystem() {
        gearPistons = new DoubleSolenoid(0, PortMap.GEAR_PISTONS[0], PortMap.GEAR_PISTONS[1]);
        gearPistons.set(DoubleSolenoid.Value.kForward);
    }

    public void setGearPosition(GearPosition position) {
        if (position == GearPosition.OPEN) {
            gearPistons.set(DoubleSolenoid.Value.kReverse);
        } else {
            gearPistons.set(DoubleSolenoid.Value.kForward);
        }
    }

    public enum GearPosition {
        OPEN, CLOSE
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

