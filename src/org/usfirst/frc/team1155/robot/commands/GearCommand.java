package org.usfirst.frc.team1155.robot.commands;

import org.usfirst.frc.team1155.robot.OI;
import org.usfirst.frc.team1155.robot.Robot;
import org.usfirst.frc.team1155.robot.subsystems.DriveSubsystem.DriveMode;
import org.usfirst.frc.team1155.robot.subsystems.GearSubsystem.GearPosition;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class GearCommand extends Command {

	private GearPosition gearPosition;
	
    public GearCommand(GearPosition position) {  
    	gearPosition = position;
    }

    // Called just before this Command runs the first time
    protected void initialize() {    	
    	Robot.gearSubsystem.setGearPosition(gearPosition);
    	//SmartDashboard.putString("Gear Position", gearPosition.name());
 		//Robot.rioDuino.SendString("depositingGear");
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
    	//LED Stuff
    	
//    	if(Robot.driveSubsystem.getDriveMode() == DriveMode.MECHANUM) {
//    		if (Robot.allianceColor == DriverStation.Alliance.Blue) {
//    			Robot.rioDuino.SendString("mechBlue");
//        	} else {
//    			Robot.rioDuino.SendString("mechRed");
//        	}
//    	}else {
//    		if (Robot.allianceColor == DriverStation.Alliance.Blue) {
//    			Robot.rioDuino.SendString("tankBlue");
//        	} else {
//    			Robot.rioDuino.SendString("tankRed");
//        	}
//    	}
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
