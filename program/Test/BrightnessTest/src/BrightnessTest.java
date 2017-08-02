import java.util.Timer;
import java.util.TimerTask;

import lejos.hardware.lcd.LCD;
import virtualDevices.ArmController;
import virtualDevices.BrightnessMeasure;
import virtualDevices.LogSender;


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
		}
		log.clear();

		while(log.connect()){
			LCD.drawString("conecting", 0, 0);
		}
		LCD.clear();
		LCD.drawString("success", 0, 0);
		Timer timer = new Timer();
		TimerTask task = new TimerTask(){
			float time = 0.00F;
			public void run(){
				log.addLog("Brightness",briMeasure.getBrightness(),time);
				time+=0.004F;
			}
		};

		timer.scheduleAtFixedRate(task, 0, 4);

		while(true){
			if(log.recieve() == "L"){
				timer.cancel();
				break;
			}
		}
		log.send();
		log.disconnect();
	}

}
