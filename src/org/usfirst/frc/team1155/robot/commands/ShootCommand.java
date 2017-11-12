package org.usfirst.frc.team1155.robot.commands;

import org.usfirst.frc.team1155.robot.OI;
import org.usfirst.frc.team1155.robot.Robot;
import org.usfirst.frc.team1155.robot.subsystems.DriveSubsystem.DriveMode;
import org.usfirst.frc.team1155.robot.subsystems.ShooterSubsystem.ServoPosition;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShootCommand extends Command {
	
	public enum ShooterSide {
		LEFT, RIGHT;
	}
	
	private ShooterSide shooterSide;

    public ShootCommand(ShooterSide side) {    	
    	shooterSide = side;
    }

    protected void initialize() {
        //Robot.shooterSubsystem.setServoPosition(ServoPosition.POSITION_1);     
        
        if(shooterSide == ShooterSide.LEFT) {
        	Robot.shooterSubsystem.setLeftShooter(0.8, -1);
        }else if(shooterSide == ShooterSide.RIGHT) {
        	Robot.shooterSubsystem.setRightShooter(0.8, 1);
        }
        Robot.rioDuino.SendString("shooting");
        //Robot.rioDuino.SendString("yellow");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
//    	if(shooterSide == ShooterSide.LEFT) {
//        	Robot.shooterSubsystem.setLeftShooter(0.8, -1);
//        }else if(shooterSide == ShooterSide.RIGHT) {
//        	Robot.shooterSubsystem.setRightShooter(0.8, 1);
//        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return (shooterSide == ShooterSide.LEFT) ? !OI.leftJoystick.getRawButton(1) : !OI.rightJoystick.getRawButton(1);
    }

    // Called once after isFinished returns true
    protected void end() {
        if(shooterSide == ShooterSide.LEFT) {
        	Robot.shooterSubsystem.leftShootTalon.set(0);
        }else if(shooterSide == ShooterSide.RIGHT) {
        	Robot.shooterSubsystem.rightShootTalon.set(0);
        }
        
        Robot.shooterSubsystem.stopAgitators();
        
        //LED Stuff
    	if(Robot.driveSubsystem.getDriveMode() == DriveMode.MECHANUM) {
    		if (Robot.allianceColor == DriverStation.Alliance.Blue) {
    			Robot.rioDuino.SendString("mechBlue");
        	} else {
    			Robot.rioDuino.SendString("mechRed");
        	}
    	}else {
    		if (Robot.allianceColor == DriverStation.Alliance.Blue) {
    			Robot.rioDuino.SendString("tankBlue");
        	} else {
    			Robot.rioDuino.SendString("tankRed");
        	}
    	}
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	
    }
}
