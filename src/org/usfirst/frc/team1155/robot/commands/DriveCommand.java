package org.usfirst.frc.team1155.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1155.robot.Robot;
import org.usfirst.frc.team1155.robot.subsystems.GlobalPositioningSubsystem.Location;

public class DriveCommand extends Command {

	
	public DriveCommand(Location mode) {
		location = mode;
		requires(Robot.googleMaps);
	}
	
	@Override
	protected void initialize() {
		Robot.googleMaps.setLocation(location);
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		switch (Robot.googleMaps.getLocation()) {
		case Gear:
			Robot.googleMaps.goSomewhere(Location.Gear);
			break;
		case Airship:
			Robot.googleMaps.goSomewhere(Location.Airship);
			break;
		case StartingPos:
			Robot.googleMaps.goSomewhere(Location.StartingPos);
			break;			
		default:
			break;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		if (Robot.googleMaps.getLocation() == Location.Gear) {
			if(Robot.René.getX() == Robot.googleMaps.gearX || Robot.René.getY() == Robot.googleMaps.gearY) {
				return true;
			} else {
				return false;
			}
		} else if (Robot.googleMaps.getLocation() == Location.Airship) {
			if(Robot.René.getX() == Robot.googleMaps.airshipX || Robot.René.getY() == Robot.googleMaps.airshipY) {
				return true;
			} else {
				return false;
			}			
		} else {
			if(Robot.René.getX() == Robot.googleMaps.startingPosX || Robot.René.getY() == Robot.googleMaps.startingPosY) {
				return true;
			} else {
				return false;
			}
		}
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}

}
