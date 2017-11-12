package org.usfirst.frc.team1155.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1155.robot.OI;
import org.usfirst.frc.team1155.robot.Robot;
import org.usfirst.frc.team1155.robot.subsystems.GearSubsystem.GearPosition;

public class DepositGearCommand extends Command {
    // Shows if the arms on the gear mechanism are open or closed
    boolean isClosed = true;

    public DepositGearCommand() {
        requires(Robot.gearSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.gearSubsystem.setGearPosition(GearPosition.OPEN);// open
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        // If the arms on the gear mechanism are closed they will open
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        // If the arms on the gear mechanism are open and the gear is not in it(meaning that it is on the airship peg) the arms will close and the command will end 
        return !OI.leftJoystick.getRawButton(2);
    }

    // Called once after isFinished returns true
    protected void end() {
        System.out.println("ended");
        Robot.gearSubsystem.setGearPosition(GearPosition.CLOSE);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
