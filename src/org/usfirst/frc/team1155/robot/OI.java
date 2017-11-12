package org.usfirst.frc.team1155.robot;

import org.usfirst.frc.team1155.robot.commands.ClimbCommand;
import org.usfirst.frc.team1155.robot.commands.DepositGearCommand;
import org.usfirst.frc.team1155.robot.commands.DistanceDriveCommand;
import org.usfirst.frc.team1155.robot.commands.DriveDegreesCommand;
import org.usfirst.frc.team1155.robot.commands.GearCommand;
import org.usfirst.frc.team1155.robot.commands.GyroTurnCommand;
import org.usfirst.frc.team1155.robot.commands.MechanumDriveCommand;
import org.usfirst.frc.team1155.robot.commands.ShootCommand;
import org.usfirst.frc.team1155.robot.commands.TankDriveCommand;
import org.usfirst.frc.team1155.robot.commands.ShootCommand.ShooterSide;
import org.usfirst.frc.team1155.robot.subsystems.GearSubsystem.GearPosition;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class OI  {

	public static Joystick leftJoystick, rightJoystick;

	public OI() {
		
		leftJoystick = new Joystick(PortMap.JOYSTICK_LEFT);
		rightJoystick = new Joystick(PortMap.JOYSTICK_RIGHT);
		
		new JoystickButton(OI.rightJoystick, 3).whenPressed(new TankDriveCommand());
		new JoystickButton(OI.rightJoystick, 4).whenPressed(new MechanumDriveCommand());
		
		new JoystickButton(OI.leftJoystick, 2).whenPressed(new DepositGearCommand());
		//new JoystickButton(OI.leftJoystick, 3).whenPressed(new GearCommand(GearPosition.CLOSE));
		
		//new JoystickButton(OI.leftJoystick, 2).whenPressed(new GearCommand(GearPosition.OPEN));
		//new JoystickButton(OI.leftJoystick, 3).whenPressed(new GearCommand(GearPosition.CLOSE));
		
		
		new JoystickButton(OI.leftJoystick, 1).whenPressed(new ShootCommand(ShooterSide.LEFT)); //drive 4 feet
		new JoystickButton(OI.rightJoystick, 1).whenPressed(new ShootCommand(ShooterSide.RIGHT)); //turn 180 degrees
		
		new JoystickButton(OI.rightJoystick, 2).whenPressed(new ClimbCommand());
	}
}