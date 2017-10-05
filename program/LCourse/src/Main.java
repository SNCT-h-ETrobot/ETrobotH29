import java.util.Timer;
import java.util.TimerTask;

import UserInterface.Starter;
import driveControl.DistanceAngleController;
import driveControl.Linetracer;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import virtualDevices.HSVColorDetector;


public class Main {

	static boolean isLinetrace;
	public static void main(String[] args) {
		Starter starter = new Starter();
		starter.calibrate();
		starter.touchStart();
		/*DistanceAngleController con = new DistanceAngleController();
		while(true)
		{
			con.goStraightAhead(5, 50);
			Delay.msDelay(1000);
		}*/
	}

}
