import lejos.hardware.lcd.LCD;
import virtualDevices.ArmController;
import virtualDevices.HSVColorDetector;
import Hardware.Hardware;


public class ColorTest {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		ArmController arm = new ArmController();
		HSVColorDetector color = new HSVColorDetector();

		int colorID;
		float[] hsv = new float[3];

		arm.controlArmNormalAngel();

		boolean flag = true;
		while(true){
			if(touchSensorIsPressed()){
				LCD.clear();
				if(flag == false){
					colorID = color.getColorID();
					LCD.drawInt(colorID, 0, 0);
					flag = true;
				}
				else{
					hsv = color.detectHSVColor();

					LCD.drawString("H:"+hsv[0], 0, 1);
					LCD.drawString("S:"+hsv[1], 0, 2);
					LCD.drawString("V:"+hsv[2], 0, 3);
					flag = false;
				}
			}
		}

	}

    public static boolean touchSensorIsPressed() {
    	float[] sampleTouch = new float[Hardware.touch.sampleSize()];
        Hardware.touchMode.fetchSample(sampleTouch , 0);
        return ((int)sampleTouch[0] != 0);
    }

}
