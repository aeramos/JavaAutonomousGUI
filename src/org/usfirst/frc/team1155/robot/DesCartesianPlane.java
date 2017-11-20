package org.usfirst.frc.team1155.robot;

import edu.wpi.first.wpilibj.ADXL345_SPI;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

class DesCartesianPlane {
    Timer timer;
    private double x = 0;
    private double y = 0;
    private double prevVx = 0;
    private double prevVy = 0;
    private double vx = 0;
    private double vy = 0;
    private double prevAx = 0;
    private double prevAy = 0;
    private ADXRS450_Gyro gyro;
    private ADXL345_SPI accelerometer;

    DesCartesianPlane(Timer timer, ADXRS450_Gyro gyro, ADXL345_SPI accelerometer) {
        this.timer = timer;
        this.gyro = gyro;
        this.accelerometer = accelerometer;
    }

    void updatePosition() {
        double ax = accelerometer.getX() * 9.80665;
        double ay = accelerometer.getX() * 9.80665;
        double dt = (timer.getTimeDifference() / 1000);
        SmartDashboard.putNumber("DB/dt", dt);
        SmartDashboard.putNumber("DB/ax", ax);
        SmartDashboard.putNumber("DB/ay", ay);
        vx += 0.5 * dt * (prevAx + ax);
        vy += 0.5 * dt * (prevAy + ay);

        x += 0.5 * dt * (prevVx + vx);
        y += 0.5 * dt * (prevVy + vy);

        prevAx = ax;
        prevAy = ay;

        prevVx = vx;
        prevVy = vy;

        System.out.println("(" + x + ", " + y + ")");
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }

    public double angleRequiredToTurn(double xDistance, double yDistance, double radius) { //Basically Bowen
        double angle = Math.toDegrees(Math.asin(xDistance / radius));
        return yDistance >= 0 ? angle : angle + 90;
    }

    public double getUsefulAngle() {
        return gyro.getAngle() > 180 ? gyro.getAngle() - 360 : gyro.getAngle();
    }

	private double netSpeed = 0;
	private double fwdRevVel = 0;
	private double strafeVel = 0;
	private double fwdRevDist = 0;
	private double strafeDist = 0;
	private double netDist = 0;


	/**
	 * Some fun math which converts the wheel speeds into robot center-of-mass motion and position
	 */

	public void update() {

		double wheelSpeedOno;
		double wheelSpeedDos;
		double wheelSpeedTres;
		double wheelSpeedCuatro;
		double Vx;
		double Vy;
		DriveSubsystem dt = Robot.driveSubsystem;

		// /////////////////////////////////////////////////////////////////////////
		// Speed calculations
		// /////////////////////////////////////////////////////////////////////////

		// Calculate wheel speeds in radians per second
		wheelSpeedOno = dt.getFrontLeftWheelSpeedRPM() * 2.0 * Math.PI / 60;
		wheelSpeedDos = dt.getFrontRightWheelSpeedRPM() * 2.0 * Math.PI / 60;
		wheelSpeedTres = dt.getRearLeftWheelSpeedRPM() * 2.0 * Math.PI / 60;
		wheelSpeedCuatro = dt.getRearRightWheelSpeedRPM() * 2.0 * Math.PI / 60;

		// Calculate translational velocity x/y components via inverse mechanum kinematic equations
		Vx = (wheelSpeedOno + wheelSpeedDos + wheelSpeedTres + wheelSpeedCuatro) * PortMap.DRIVETRAIN_WHEELS_RADIUS_FT /*wheel radius in feet*/ / 4;
		Vy = (wheelSpeedOno - wheelSpeedDos + wheelSpeedTres - wheelSpeedCuatro) * PortMap.DRIVETRAIN_WHEELS_RADIUS_FT /*wheel radius in feet*/ / 4;

		// Calculate net speed vector with pythagorean theorem
		netSpeed = Math.sqrt(Vx * Vx + Vy * Vy);

		// Store results into state variables
		fwdRevVel = Vx;
		strafeVel = Vy;

		// /////////////////////////////////////////////////////////////////////////
		// Distance calculations - similar to above
		// /////////////////////////////////////////////////////////////////////////

		fwdRevDist = (dt.getFrontLeftWheelDistanceFt() +
				dt.getFrontRightWheelDistanceFt() +
				dt.getRearLeftWheelDistanceFt() +
				dt.getRearRightWheelDistanceFt()) / 4.0;
		strafeDist = (dt.getFrontLeftWheelDistanceFt() -
				dt.getFrontRightWheelDistanceFt() +
				dt.getRearLeftWheelDistanceFt() -
				dt.getRearRightWheelDistanceFt()) / 4.0;

		netDist += netSpeed * 0.02; // meh, just a guess at sample time. This isn't used for anything now that I know of.
	}

	public double getNetSpeedFtPerS() {
		return netSpeed;
	}

	public double getFwdRevVelFtPerS() {
		return fwdRevVel;
	}

	public double getStrafeVelFtPerS() {
		return strafeVel;
	}

	public double getFwdRevDistFt() {
		return fwdRevDist;
	}

	public double getStrafeDistFt() {
		return strafeDist;
	}

	public double getNetDistFt() {
		return netDist;
	}

}
