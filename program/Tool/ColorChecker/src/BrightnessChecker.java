import virtualDevices.ArmController;
import lejos.hardware.lcd.LCD;
import Hardware.Hardware;

/* 輝度値チェッカー
 * 会場でコースの輝度値調べる用
 * 最初にアームキャリブレーション処理が入ります
 * */
public class BrightnessChecker {
	static ArmController arm;
	public static void main(String[] args) {
		arm = new ArmController();
		float[] bright = new float[Hardware.colorSensor.getRedMode().sampleSize()];
		while(true){
			arm.controlArmNormalAngel();
			Hardware.colorSensor.getRedMode().fetchSample(bright, 0);
			float[] sampleTouch = new float[Hardware.touch.sampleSize()];
			Hardware.touchMode.fetchSample(sampleTouch, 0);
			if(sampleTouch[0] != 0){
				LCD.drawString("bright:"+bright[0], 0, 0);
			}
		}
	}


}
