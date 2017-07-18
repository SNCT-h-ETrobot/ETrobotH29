import lejos.hardware.lcd.LCD;
import Hardware.Hardware;


public class ColorChecker {

	public static void main(String[] args) {
		float[] bright = new float[Hardware.colorSensor.getRGBMode().sampleSize()];
		while(true){
			Hardware.colorSensor.getRGBMode().fetchSample(bright, 0);
			float[] sampleTouch = new float[Hardware.touch.sampleSize()];
			Hardware.touchMode.fetchSample(sampleTouch, 0);
			if(sampleTouch[0] != 0){
				LCD.drawString("r:"+bright[0], 0, 0);
				LCD.drawString("g:"+bright[1], 0, 1);
				LCD.drawString("b:"+bright[2], 0, 2);
			}
		}
	}


}
