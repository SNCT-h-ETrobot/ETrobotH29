package driveControl;

import java.util.Timer;
import java.util.TimerTask;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import virtualDevices.BrightnessMeasure;
import virtualDevices.DistanceMeasure;
import Hardware.Hardware;
//import ev3Viewer.LogSender;

/**
 * 灰色検知する
 * レース区間終了から、灰色を検知し、レール前まで進んで停止する
 * */
public class DetectGray{
	private static final float TARGET_DISTANCE = 112.0f;//灰色終端からレール前までの距離
	private static final float TARGET_SPEED = 60.0f;
	private static final float TARGET_SPEED_HI = 70.0f;

	private static final float LT_P = -80.0f;
	private static final float LT_I = -10.0f;
	private static final float LT_D = -5.0f;
	private static final float LT_BRIGHT = 0.58f;//灰色より若干低い値
	private static final float LT_BRIGHT_2 = 0.35f;//灰色より若干低い値
	private static final float LT_BRIGHT_3 = 0.5f;//灰色より若干低い値

	private static final float GRAY_THRESHOLD = 0.4f;//一定期間でこれだけ変化したら判定
	private static final int GRAY_QUEUE = 40;

	//0.02 0.28 0.50

	private DistanceAngleController DAC;
	private DistanceMeasure dm;
	private Linetracer lt;
	private BrightnessMeasure bm;

	private float[] bright;
	private float targetBright;
	private float speed;
	private float ltP;
	private float ltI;
	private float ltD;

	public DetectGray() {
		DAC = new DistanceAngleController();
		dm = new DistanceMeasure();
		lt = new Linetracer();
		bm = new BrightnessMeasure();
		bright = new float[GRAY_QUEUE];
		ltP = LT_P;
		ltI = LT_I;
		ltD = LT_D;
	}

	public void run() {
		for (float i : bright) {
			i = 0;
		}
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask(){
			public void run(){
				lt.linetrace(ltP, ltI, ltD, targetBright, speed);
			}
		};

		//LogSender log = new LogSender();
		LCD.drawString("connect", 0, 1);
		//log.connect();

		timer.scheduleAtFixedRate(timerTask, 0, 4);

		int a = 0;
		for (int i = 0; i < bright.length; i++) {
			bright[i] = bm.getNormalizedBrightness();
		}
		dm.resetDistance();
		long time = System.nanoTime();
		targetBright = LT_BRIGHT;
		speed = TARGET_SPEED;
		while(true){
			LCD.drawString("distance:"+dm.getDistance(), 0, 1);
			bright[a] = bm.getNormalizedBrightness();
			float nowtime = (System.nanoTime() - time)/1000000000.0f;
			float newer = 0,older = 0;
			for(int i = 0;i<10;++i){
				newer += bright[(a+GRAY_QUEUE-i)%GRAY_QUEUE];
				older += bright[(a+ 1+i)%GRAY_QUEUE];
			}
			newer/=10.0f;older/=10.0f;
			//if(newer < LT_BRIGHT && LT_BRIGHT < older + 0.1 && older - 0.1 < LT_BRIGHT && newer < older - GRAY_THRESHOLD)
			/*log.addLog("bright", bright[a], nowtime);
			log.addLog("newer", newer, nowtime);
			log.addLog("older", older, nowtime);
			log.addLog("distance", newer - older, nowtime);*/
			if(dm.getDistance() > 25.0f && newer - older > GRAY_THRESHOLD)
				break;
			a = (a+1)%GRAY_QUEUE;
			Delay.msDelay(4);
		}
		//検知後少し走行することで、左側へ逸れないようにする
		dm.resetDistance();
		while(dm.getDistance() < 10.0f )
		{
			Delay.msDelay(4);
		}
		
		//右側トレースへの切り替え
		dm.resetDistance();
		targetBright = LT_BRIGHT_2;
		speed = TARGET_SPEED_HI;
		ltP = 60.0F;
		ltI = 5.0F;
		ltD = 5.0F;
		while(dm.getDistance() < 10.0F){
			Delay.msDelay(4);
		}
		//カーブを曲がり切れる値
		dm.resetDistance();
		targetBright = LT_BRIGHT_3;
		ltP = 100.0F;
		ltI = 60.0F;
		ltD = 5.0F;
		while(dm.getDistance() < TARGET_DISTANCE){
			Delay.msDelay(4);
		}
		
		timer.cancel();

		for (int i = 0; i < 10; i++) {
			Hardware.motorPortL.controlMotor(0, 1);
			Hardware.motorPortR.controlMotor(0, 1);
			Delay.msDelay(4);
		}
		/*log.send();
		for (int i = 0; i < 10000; i++) {
			Hardware.motorPortL.controlMotor(0, 1);
			Hardware.motorPortR.controlMotor(0, 1);
			Delay.msDelay(4);
		}*/


	}

}