package org.usfirst.frc.team1155.robot;

import edu.wpi.first.wpilibj.I2C;

public class RioDuinoController {

	private I2C i2cBus;
	String rioDuinoDrive;
	
	public RioDuinoController()
	{
		i2cBus = new I2C(I2C.Port.kMXP, 4);
	}
	
	public void SendStateChange(char state)
	{
		i2cBus.write(0x02, state);
		//causes color to update
	}
	
	public String getRioDuinoDrive(){
		return rioDuinoDrive;
	}
	
	public void setRioDuinoDrive(String set){
		rioDuinoDrive = set;
	}
	
	public void SendString(String writeStr)
	{
		byte[] toSend = new byte[1];
		switch(writeStr){
		case ("autoBlue"):
			toSend[0] = 0;
			break;
		case ("autoRed"):
			toSend[0] = 1;
			break;
		case ("tankRed"):
			toSend[0] = 3;
			break;
		case ("mechBlue"):
			toSend[0] = 4;
			break;
		case ("mechRed"):
			toSend[0] = 5;
			break;
		case ("disableInit"):
			toSend[0] = 6;
			break;
		case ("depositingGear"):
			toSend[0] = 7;
			break;
		case ("shooting"):
			toSend[0] = 8;
			break;
		case ("climbing"):
			toSend[0] = 9;
			break;
		case ("red"):
			toSend[0] = 10;
			break;
		case ("yellow"):
			toSend[0] = 11;
			break;
		case ("green"):
			toSend[0] = 12;
			break;
		case ("blue"):
			toSend[0] = 13;
			break;
		case ("purple"):
			toSend[0] = 14;
			break;
		}
		//i2cBus.transaction(toSend, toSend.length, null, 0);
		//sends it to RioDuino
	}
}