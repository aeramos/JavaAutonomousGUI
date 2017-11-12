package org.usfirst.frc.team1155.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1155.robot.Robot;

class AutoShootCommand extends Command {
    protected void initialize() {
        Robot.shooterSubsystem.setLeftShooter(0.8, -1);
        Robot.shooterSubsystem.setRightShooter(0.8, 1);
        Robot.rioDuino.SendString("shooting");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.shooterSubsystem.setLeftShooter(0.8, -1);
        Robot.shooterSubsystem.setRightShooter(0.8, 1);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.isInTeleop;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.shooterSubsystem.leftShootTalon.set(0);
        Robot.shooterSubsystem.rightShootTalon.set(0);
        Robot.shooterSubsystem.stopAgitators();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
