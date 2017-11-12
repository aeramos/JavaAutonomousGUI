package org.usfirst.frc.team1155.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1155.robot.OI;
import org.usfirst.frc.team1155.robot.Robot;
import org.usfirst.frc.team1155.robot.subsystems.DriveSubsystem.DriveMode;

public class MecanumDriveCommand extends Command {
    private Joystick lateralMovement, rotationalMovement;

    public MecanumDriveCommand() {
        lateralMovement = OI.rightJoystick;
        rotationalMovement = OI.leftJoystick;
    }

    @Override
    protected void initialize() {
        System.out.println("Mech Drive COmmand");

        Robot.driveSubsystem.setDriveMode(DriveMode.MECANUM);
        Robot.driveSubsystem.setMechSpeed(0, 0, 0);

        if (Robot.allianceColor == DriverStation.Alliance.Blue) {
            Robot.rioDuino.SendString("mechBlue");
        } else {
            Robot.rioDuino.SendString("mechRed");
        }
    }

    @Override
    protected void execute() {
        Robot.driveSubsystem.setSpeed(lateralMovement, rotationalMovement);
    }

    @Override
    protected boolean isFinished() {
        return Robot.driveSubsystem.getDriveMode() != DriveMode.MECANUM;
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
    }
}
