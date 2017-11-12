package org.usfirst.frc.team1155.robot.commands;

import org.usfirst.frc.team1155.robot.OI;
import org.usfirst.frc.team1155.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClimbCommand extends Command {

    // Called just before this Command runs the first time
	
    protected void initialize() {
    	Robot.climberSubsystem.startClimb();
    	
    	//Robot.rioDuino.SendString("climbing");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return !OI.rightJoystick.getRawButton(2);
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.climberSubsystem.stopClimbing();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
