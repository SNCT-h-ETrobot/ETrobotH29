package driveControl;

import java.util.Timer;
import java.util.TimerTask;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import virtualDevices.ArmController;
import virtualDevices.BrightnessMeasure;
import virtualDevices.DistanceMeasure;
import Hardware.Hardware;
//import ev3Viewer.LogSender;

/**
 * 灰色検知する
 * レース区間終了から、灰色を検知し、レール前まで進んで停止する
 * */
public class DetectGray{
	private static final float TARGET_DISTANCE = 100.0f;//灰色終端からレール前までの距離
	private static final float TARGET_SPEED = 60.0f;
	private static final float TARGET_SPEED_HI = 80.0f;

	private static final float LT_P = -100.0f;
	private static final float LT_I = -10.0f;
	private static final float LT_D = -5.0f;
	private static final float LT_BRIGHT = 0.58f;//0.62f;//灰色より若干低い値
	private static final float LT_P_2 = 120.0f;
	private static final float LT_I_2 = 30.0f;
	private static final float LT_D_2 = 5.0f;
	private static final float LT_BRIGHT_2 = 0.5f;//

	private static final float GRAY_THRESHOLD = 0.3f;//一定期間でこれだけ変化したら判定
	private static final int GRAY_QUEUE = 40;

	//0.02 0.28 0.50

	private DistanceAngleController DAC;
	private DistanceMeasure dm;
	private Linetracer lt;
	private BrightnessMeasure bm;
	private ArmController arm;

	private float[] bright;
	private float ltP;
	private float ltI;
	private float ltD;
	private float targetBright;
	private float speed;
	private boolean isLT;

	public DetectGray() {
		DAC = new DistanceAngleController();
		dm = new DistanceMeasure();
		lt = new Linetracer();
		bm = new BrightnessMeasure();
		bright = new float[GRAY_QUEUE];
		arm = new ArmController();
	}
	public void run() {
		for (float i : bright) {
			i = 0;
		}
		isLT = true;
		ltP = LT_P;
		ltI = LT_I;
		ltD = LT_D;
		targetBright = LT_BRIGHT;
		speed = TARGET_SPEED;
		arm.controlArmNormalAngel();
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask(){
			public void run(){
				if(isLT)lt.linetrace(ltP, ltI, ltD, targetBright, speed);
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
		dm.resetDistance();
		while(dm.getDistance() < 10){
			Delay.msDelay(4);
		}
		isLT = false;
		DAC.turn(20, false);
		isLT = true;
		ltP = LT_P_2;
		ltI = LT_I_2;
		ltD = LT_D_2;
		targetBright = LT_BRIGHT_2;
		speed = TARGET_SPEED_HI;
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
