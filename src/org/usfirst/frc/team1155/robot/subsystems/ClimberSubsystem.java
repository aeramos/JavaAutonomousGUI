package org.usfirst.frc.team1155.robot.subsystems;

import org.usfirst.frc.team1155.robot.PortMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class ClimberSubsystem extends Subsystem {

    public CANTalon leftClimber, rightClimber;

    public ClimberSubsystem() {
    	leftClimber = new CANTalon(PortMap.LEFT_CLIMBER_TALON);
    	rightClimber = new CANTalon(PortMap.RIGHT_CLIMBER_TALON);
    }
    
    public void startClimb() {
    	leftClimber.set(1);
    	rightClimber.set(1); //change one to negative
    }
    
    public void stopClimbing() {
    	leftClimber.set(0);
    	rightClimber.set(0);
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

