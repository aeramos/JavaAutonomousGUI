package org.usfirst.frc.team1155.robot;

class Timer {
    private double startTime, previousTime, currentTime, differenceInTime;

    // Called During Auto-Init
    void setStartTime() {
        startTime = System.currentTimeMillis();
        previousTime = startTime;
        System.out.println("Start Time " + startTime);
    }

    public double getStartTime() {
        return startTime;
    }

    private double getPreviousTime() {
        if (currentTime != 0.0) {
            previousTime = currentTime;
            return previousTime;
        }
        return previousTime;
    }

    private double getCurrentTime() {
        currentTime = System.currentTimeMillis();
        return currentTime;
    }

    double getTimeDifference() {
        differenceInTime = -getPreviousTime() + getCurrentTime();
        return differenceInTime;
    }
}
