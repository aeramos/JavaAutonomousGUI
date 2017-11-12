package org.usfirst.frc.team1155.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1155.robot.Robot;

class VisionAlignCommand extends Command {
    private final double MIN_PEG_DISTANCE = 1; //inches
    private final double MIN_ANGLE = 2; //degrees
    private double visionDistance;
    private double visionAngle;
    private boolean finishedRotating;

    public VisionAlignCommand() {
        requires(Robot.driveSubsystem);
        finishedRotating = false;
    }

    // Called just before this Command runs the first time
    @SuppressWarnings("deprecation")
    protected void initialize() {
        Robot.gyro.reset();

        try {
            if (Math.abs(visionAngle) > MIN_ANGLE) {
                new GyroTurnCommand(visionAngle).start();
                finishedRotating = true;
            }
        } catch (Exception e) {
            System.out.println("Vision not working properly during init");
        }
    }

    // Called repeatedly when this Command is scheduled to run
    @SuppressWarnings("deprecation")
    protected void execute() {
        try {
            if ((Math.abs(visionAngle) > MIN_ANGLE) && !finishedRotating) {
                new GyroTurnCommand(visionAngle).start();
                finishedRotating = true;
            }
        } catch (Exception e) {
            System.out.println("Vision not working properly during execute");
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    //Check to see whether it is both within angle buffer from tape and
    //within the distance
    protected boolean isFinished() {
        return (visionDistance <= MIN_PEG_DISTANCE) && (Math.abs(visionAngle) < MIN_ANGLE);
    }

    // Called once after isFinished returns true
    protected void end() {
        System.out.println("finished vision align");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
