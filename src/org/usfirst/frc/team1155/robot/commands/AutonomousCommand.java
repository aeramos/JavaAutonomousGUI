package org.usfirst.frc.team1155.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team1155.robot.Robot;
import org.usfirst.frc.team1155.robot.subsystems.DriveSubsystem.DriveMode;

public class AutonomousCommand extends CommandGroup {
    private final double DISTANCE_TO_BASELINE = 93.25; // inches
    private final double DISTANCE_TO_PIVOT = 20.35; // inches
    private final double ROBOT_LENGTH = 36;
    private final double ROBOT_WIDTH = 0;

    // possible order of auto commands
    public AutonomousCommand(AutoRoutine pos) {
        Robot.driveSubsystem.setDriveMode(DriveMode.TANK);
        switch (pos) {
            case GEAR_LEFT:
                addSequential(new DistanceDriveCommand(96 - ROBOT_LENGTH / 2));
                addSequential(new GyroTurnCommand(60));
                addSequential(new DistanceDriveCommand(62 - ROBOT_LENGTH / 3));
                break;
            case GEAR_RIGHT:
                addSequential(new DistanceDriveCommand(96 - ROBOT_LENGTH / 2));
                addSequential(new GyroTurnCommand(-60));
                addSequential(new DistanceDriveCommand(62 - ROBOT_LENGTH / 3));
                break;
            case GEAR_MIDDLE:
                addSequential(new DistanceDriveCommand(110 - ROBOT_LENGTH));//DISTANCE_TO_BASELINE + DISTANCE_TO_PIVOT));
                break;
            case SHOOT_RED:
                addSequential(new DistanceDriveCommand(-59 + ROBOT_LENGTH / 2));
                addSequential(new GyroTurnCommand(-45));
                addSequential(new AutoShootCommand());

                break;
            case SHOOT_BLUE:
                addSequential(new DistanceDriveCommand(-59 + ROBOT_LENGTH / 2));
                addSequential(new GyroTurnCommand(45));
                addSequential(new AutoShootCommand());
                addSequential(new AutoShootCommand());
                break;
            case BASELINE:
                addSequential(new DistanceDriveCommand(DISTANCE_TO_BASELINE));
            default:
                break;
        }
    }

    public enum AutoRoutine {
        GEAR_LEFT, // left driver station
        GEAR_MIDDLE, // middle driver station
        GEAR_RIGHT,
        SHOOT_RED,
        SHOOT_BLUE,
        NOTHING,
        BASELINE // right driver station
    }
}
