package org.usfirst.frc.team1155.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1155.robot.Robot;
import org.usfirst.frc.team1155.robot.subsystems.DriveSubsystem.SensorMode;

class GyroTurnCommand extends Command {
    private double initialAngle, degreesToTurn;
    private boolean sketch;

    GyroTurnCommand(double degrees) {
        degreesToTurn = degrees;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        sketch = false;
        System.out.println("turning " + degreesToTurn);

        initialAngle = Robot.gyro.getAngle();

        Robot.driveSubsystem.sensorMode = SensorMode.GYRO;

        Robot.driveSubsystem.startAdjustment(initialAngle, initialAngle + degreesToTurn);
        // Sets PID of the PID controller to the values given on the SmartDashboard
        Robot.driveSubsystem.getPIDController().setAbsoluteTolerance(1);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        double avg = Robot.driveSubsystem.getPIDController().getAvgError();
        if (avg != 0) sketch = true;
        System.out.println(avg);
        return Robot.driveSubsystem.getPIDController().onTarget();
    }

    // Called once after isFi nished returns true
    protected void end() {
        System.out.println("FInished gyro");
        Robot.driveSubsystem.setTankSpeed(0, 0);
        Robot.driveSubsystem.endAdjustment();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
