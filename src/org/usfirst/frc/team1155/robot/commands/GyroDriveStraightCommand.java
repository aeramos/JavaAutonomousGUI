package org.usfirst.frc.team1155.robot.commands;

import org.usfirst.frc.team1155.robot.OI;
import org.usfirst.frc.team1155.robot.Robot;
import org.usfirst.frc.team1155.robot.subsystems.DriveSubsystem.DriveMode;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GyroDriveStraightCommand extends Command {

	private double speed;
	private double targetAngle;
	private final double ANGLE_BUFFER = 2;
	private final double SPEED_CORRECTION = 0.15;
	
    public GyroDriveStraightCommand(double driveSpeed) {
        speed = driveSpeed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	targetAngle = Robot.gyro.getAngle();
    	Robot.driveSubsystem.setDriveMode(DriveMode.TANK);
    	Robot.driveSubsystem.setTankSpeed(speed, speed);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (Robot.gyro.getAngle() - targetAngle > ANGLE_BUFFER){ //rotating counter-clockwise
    		Robot.driveSubsystem.setTankSpeed(speed + SPEED_CORRECTION, speed);
    	}else if (targetAngle - Robot.gyro.getAngle() > ANGLE_BUFFER){ //rotating -clockwise 
    		Robot.driveSubsystem.setTankSpeed(speed, speed + SPEED_CORRECTION);
    	}else {
        	Robot.driveSubsystem.setTankSpeed(speed, speed);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return !OI.rightJoystick.getRawButton(1);
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveSubsystem.setTankSpeed(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {

    }
}
