package org.usfirst.frc.team1155.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1155.robot.OI;
import org.usfirst.frc.team1155.robot.Robot;
import org.usfirst.frc.team1155.robot.subsystems.GearSubsystem.GearPosition;

class GearCommand extends Command {
    private GearPosition gearPosition;

    public GearCommand(GearPosition position) {
        gearPosition = position;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.gearSubsystem.setGearPosition(gearPosition);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (gearPosition == GearPosition.OPEN) ? !OI.leftJoystick.getRawButton(4) : !OI.leftJoystick.getRawButton(3);
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
