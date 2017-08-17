import java.util.Timer;
import java.util.TimerTask;

import lejos.hardware.lcd.LCD;
import virtualDevices.ArmController;
import virtualDevices.BrightnessMeasure;
import virtualDevices.LogSender;
import Hardware.Hardware;


public class BrightnessTest {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		ArmController arm = new ArmController();
		final BrightnessMeasure briMeasure = new BrightnessMeasure();
		final LogSender log = new LogSender();

		arm.controlArmNormalAngel();
		for(int i = 0;i<1500;i++){
			//条件合わせるため空実行
			briMeasure.getBrightness();
			log.addLog("dammy", 0.0F, 0.1F);
			Hardware.motorPortL.controlMotor(0, 1);
			Hardware.motorPortR.controlMotor(0, 1);
		}
		log.clear();

		LCD.drawString("conecting", 0, 0);
		log.connect();

		LCD.clear();
		LCD.drawString("success", 0, 0);

		Timer timer = new Timer();
		TimerTask task = new TimerTask(){
			float time = 0.000F;
			public void run(){

				Hardware.motorPortL.controlMotor(30, 1);
				Hardware.motorPortR.controlMotor(30, 1);
				log.addLog("Brightness",briMeasure.getBrightness(),time);
				log.addLog("BrightnessDirect",briMeasure.getBrightnessDirect(),time);
				time+=0.004F;
			}
		};

		timer.scheduleAtFixedRate(task, 0, 4);

		while(true){
			if(touchSensorIsPressed()){
				timer.cancel();

				Hardware.motorPortL.controlMotor(0, 1);
				Hardware.motorPortR.controlMotor(0, 1);
				LCD.clear();
				LCD.drawString("END", 0, 0);

				log.send();
				break;
			}
		}

		log.disconnect();
	}

    public static boolean touchSensorIsPressed() {
    	float[] sampleTouch = new float[Hardware.touch.sampleSize()];
        Hardware.touchMode.fetchSample(sampleTouch , 0);
        return ((int)sampleTouch[0] != 0);
    }

}
