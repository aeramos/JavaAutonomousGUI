package org.usfirst.frc.team1155.robot.commands;

import org.usfirst.frc.team1155.robot.Robot;
import org.usfirst.frc.team1155.robot.subsystems.DriveSubsystem.SensorMode;

import edu.wpi.first.wpilibj.command.Command;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DistanceDriveCommand extends Command {
	
	public double distanceToDrive;
	
	//distance in inches
	public DistanceDriveCommand(double distance) {
        requires(Robot.driveSubsystem);

        distanceToDrive = distance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.driveSubsystem.resetEncoders();
    	
    	Robot.driveSubsystem.sensorMode = SensorMode.ENCODER;
    	
		Robot.driveSubsystem.startAdjustment(Robot.driveSubsystem.getEncDistance(), distanceToDrive);
		//Robot.driveSubsystem.getPIDController().setPID(SmartDashboard.getNumber("P", 0.1), SmartDashboard.getNumber("I", 0), SmartDashboard.getNumber("D", 0.1));
		Robot.driveSubsystem.getPIDController().setAbsoluteTolerance(2);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		//SmartDashboard.putNumber("EncoderValue", Robot.driveSubsystem.getEncDistance());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	System.out.println(Robot.driveSubsystem.getEncDistance());
    	return Robot.driveSubsystem.getPIDController().onTarget();// || Robot.driveSubsystem.getRightUltrasonic() < 2;
    	
    	//return Math.abs(Robot.driveSubsystem.getPIDController().getError()) < 3;
    }

    // Called once after isFinished returns true
    protected void end() {
    	System.out.println("Finished");
    	Robot.driveSubsystem.endAdjustment();
    	Robot.driveSubsystem.setTankSpeed(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
