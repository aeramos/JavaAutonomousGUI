package org.usfirst.frc.team1155.robot.commands;

import org.usfirst.frc.team1155.robot.OI;
import org.usfirst.frc.team1155.robot.Robot;
import org.usfirst.frc.team1155.robot.subsystems.DriveSubsystem.DriveMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MechanumDriveCommand extends Command {

	private Joystick lateralMovement, rotationalMovement;
	
    public MechanumDriveCommand() {        
        lateralMovement = OI.rightJoystick;
        rotationalMovement = OI.leftJoystick;
    }

    @Override
	protected void initialize() {
		System.out.println("Mech Drive COmmand");

    	Robot.driveSubsystem.setDriveMode(DriveMode.MECHANUM);
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
        return Robot.driveSubsystem.getDriveMode() != DriveMode.MECHANUM;
    }

    @Override
	protected void end() {
    	
    }

    @Override
	protected void interrupted() {
    	
    }
}
