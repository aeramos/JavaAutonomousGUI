package org.usfirst.frc.team1155.robot.commands;

import org.usfirst.frc.team1155.robot.Robot;
import org.usfirst.frc.team1155.robot.commands.ShootCommand.ShooterSide;
import org.usfirst.frc.team1155.robot.subsystems.DriveSubsystem.DriveMode;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonomousCommand extends CommandGroup {

	// private boolean crossBorder = false;
	// private boolean shootBalls = false;
	private final double DISTANCE_TO_BASELINE = 93.25; // inches
	private final double DISTANCE_TO_PIVOT = 20.35; // inches
	private final double ROBOT_LENGTH = 36;
	private final double ROBOT_WIDTH = 0;
	
	public enum AutoRoutine {
		GEAR_LEFT, // left driver station
		GEAR_MIDDLE, // middle driver station
		GEAR_RIGHT,
		SHOOT_RED,
		SHOOT_BLUE,
		NOTHING, 
		BASELINE; // right driver station
	}

	// possible order of auto commands
	public AutonomousCommand(AutoRoutine pos) {
		Robot.driveSubsystem.setDriveMode(DriveMode.TANK);
		switch (pos) {
		case GEAR_LEFT:
			//System.out.println("gear left");
			addSequential(new DistanceDriveCommand(96 - ROBOT_LENGTH/2));
			addSequential(new GyroTurnCommand(60));
			addSequential(new DistanceDriveCommand(62 - ROBOT_LENGTH/3));
			break;
		case GEAR_RIGHT:
			addSequential(new DistanceDriveCommand(96 - ROBOT_LENGTH/2));
			addSequential(new GyroTurnCommand(-60));
			addSequential(new DistanceDriveCommand(62 - ROBOT_LENGTH/3));
			//addSequential(new DepositGearCommand());
			break;
		case GEAR_MIDDLE:
			addSequential(new DistanceDriveCommand(110 - ROBOT_LENGTH));//DISTANCE_TO_BASELINE + DISTANCE_TO_PIVOT));
			//addSequential(new DepositGearCommand());
			//addSequential(new DistanceDriveCommand(-50));
			break;
		case SHOOT_RED:
//			addSequential(new ShootCommand(ShooterSide.LEFT));
			
			addSequential(new DistanceDriveCommand(-59 + ROBOT_LENGTH/2));
			addSequential(new GyroTurnCommand(-45));
			addSequential(new AutoShootCommand());
			
			break;
		case SHOOT_BLUE:
			addSequential(new DistanceDriveCommand(-59 + ROBOT_LENGTH/2));
			addSequential(new GyroTurnCommand(45));
			addSequential(new AutoShootCommand());
																																																																																																																																																																																addSequential(new AutoShootCommand());
			break;
		case BASELINE:
			addSequential(new DistanceDriveCommand(DISTANCE_TO_BASELINE));
			
		default:
			break;
		}

		/*
		 * addSequential(new DistanceDriveCommand()); addSequential(new
		 * DepositGearCommand()); if (crossBorder) { addSequential(new
		 * DriveCommand()); } if (shootBalls) { addSequential(new
		 * DriveCommand()); addSeuqential(new ShootCommand()); }
		 * addSequential(new PositionDriveCommand());
		 */
	}
}
