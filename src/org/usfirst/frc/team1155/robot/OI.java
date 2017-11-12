package org.usfirst.frc.team1155.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.usfirst.frc.team1155.robot.commands.*;
import org.usfirst.frc.team1155.robot.commands.ShootCommand.ShooterSide;

public class OI {
    public static Joystick leftJoystick, rightJoystick;

    public OI() {
        leftJoystick = new Joystick(PortMap.JOYSTICK_LEFT);
        rightJoystick = new Joystick(PortMap.JOYSTICK_RIGHT);

        new JoystickButton(OI.rightJoystick, 3).whenPressed(new TankDriveCommand());
        new JoystickButton(OI.rightJoystick, 4).whenPressed(new MecanumDriveCommand());

        new JoystickButton(OI.leftJoystick, 2).whenPressed(new DepositGearCommand());

        new JoystickButton(OI.leftJoystick, 1).whenPressed(new ShootCommand(ShooterSide.LEFT)); //drive 4 feet
        new JoystickButton(OI.rightJoystick, 1).whenPressed(new ShootCommand(ShooterSide.RIGHT)); //turn 180 degrees

        new JoystickButton(OI.rightJoystick, 2).whenPressed(new ClimbCommand());
    }
}