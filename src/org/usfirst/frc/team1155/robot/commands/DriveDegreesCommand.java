package org.usfirst.frc.team1155.robot.commands;

import org.usfirst.frc.team1155.robot.OI;
import org.usfirst.frc.team1155.robot.Robot;
import org.usfirst.frc.team1155.robot.subsystems.DriveSubsystem.DriveMode;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveDegreesCommand extends Command {

	private double degrees;
	
    public DriveDegreesCommand(double degrees) {
        this.degrees = degrees;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.driveSubsystem.setDriveMode(DriveMode.MECHANUM);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//System.out.println(degrees);
    	Robot.driveSubsystem.moveDegrees(degrees);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return !OI.leftJoystick.getRawButton(3) && !OI.leftJoystick.getRawButton(4); //change to buttons
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveSubsystem.angleDrive = false;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
