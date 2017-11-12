package org.usfirst.frc.team1155.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1155.robot.Robot;
import org.usfirst.frc.team1155.robot.subsystems.DriveSubsystem.SensorMode;

class DistanceDriveCommand extends Command {
    private double distanceToDrive;

    //distance in inches
    DistanceDriveCommand(double distance) {
        requires(Robot.driveSubsystem);

        distanceToDrive = distance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.driveSubsystem.resetEncoders();

        Robot.driveSubsystem.sensorMode = SensorMode.ENCODER;

        Robot.driveSubsystem.startAdjustment(Robot.driveSubsystem.getEncDistance(), distanceToDrive);
        Robot.driveSubsystem.getPIDController().setAbsoluteTolerance(2);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        System.out.println(Robot.driveSubsystem.getEncDistance());
        return Robot.driveSubsystem.getPIDController().onTarget();
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
