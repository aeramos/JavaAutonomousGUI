package org.usfirst.frc.team1155.robot;

public class Timer{

	private double startTime, previousTime, currentTime, differenceInTime;
	
	public Timer(){
		// It's funny how the colors of the real world only seem really real when you viddy them on the screen.
	}
	
	// Called During Auto-Init
	public void setStartTime() {
		startTime = System.currentTimeMillis();
		previousTime = startTime;
		System.out.println("Start Time " + startTime);
	}

	public double getStartTime(){
		return startTime;
	}

	
	public double getPreviousTime() {
		if (currentTime != 0.0) {
			previousTime = currentTime;
			return previousTime;
		}
		return previousTime;
	}
	
	public double getCurrentTime() {
		currentTime = System.currentTimeMillis();	
		return currentTime;		
	}
	
	public double getTimeDifference() {
		differenceInTime = -getPreviousTime() + getCurrentTime();
		return differenceInTime;
	}
	
}
