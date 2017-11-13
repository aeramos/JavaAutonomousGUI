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
}
