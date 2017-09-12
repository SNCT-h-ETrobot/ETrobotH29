import java.util.Timer;
import java.util.TimerTask;

import lejos.utility.Delay;
import virtualDevices.ArmController;
import virtualDevices.BrightnessMeasure;
import virtualDevices.DistanceMeasure;
import driveControl.DistanceAngleController;
import driveControl.Linetracer;


public class GarageTestL {

	//元からあるので移植しなくていい部分
	private static float currentAngle;
	private static ArmController arm;
	private static Linetracer linetracer;
	private static DistanceMeasure distMeasure;
	private static DistanceAngleController dACtrl;
	private static final float P_GAIN = 50.0F;	//P係数
	private static final float I_GAIN = 10.0F;	//I係数
	private static final float D_GAIN = 5.0F;	//D係数
	private static final float TARGET_BRIGHTNESS = 0.5F;

	//追加したプロパティ。staticははずして移植
	private static BrightnessMeasure briMeasure;
	private static float kp;
	private static float ki;
	private static float kd;
	private static float targetForward;
	private static boolean isLinetrace;
	private static final float TURN_TO_GRAY_DISTANCE = 50.0F;
	private static final float LINE_TO_GARAGE_DISTANCE = 65.0F;

	public static void main(String[] args) {
		//Lコースのほう。指定場所においてスタート
		arm = new ArmController();
		linetracer = new Linetracer();
		distMeasure = new DistanceMeasure();
		dACtrl = new DistanceAngleController();
		briMeasure = new BrightnessMeasure();

		arm.controlArmNormalAngel();
		for(int i =0;i<1500;i++){
			//空実行部
			linetracer.linetrace(0, 0, 0, 0, 0);
			distMeasure.getDistance();
			briMeasure.getNormalizedBrightness();
		}

		currentAngle = 15.000F;

		parking();

	}


	//クラス化しない。このメソッドだけstatic消してGamePartDriverに移植する
	private static void parking(){
		//15度方向を向く（縦列駐車前の線に垂直）
		if(currentAngle != 15.000F){//仮想頂点から来たとき
			//青頂点から来たとき
			if(currentAngle == 105.000F){
				dACtrl.turn(-90.000F, false);
			}
			//緑頂点から来たとき
			else if(currentAngle == 285.000F){
				dACtrl.turn(90.000F, false);
			}
		}
		//ライントレース部（isLinetraceによって実行したりしなかったりする）
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask(){
			public void run(){
				if(isLinetrace)linetracer.linetrace(kp, ki, kd, TARGET_BRIGHTNESS, targetForward);
			}
		};

		//線を検知するまで進む
		isLinetrace = true;
		kp = 0.0F;
		ki = 0.0F;
		kd = 0.0F;
		targetForward  = 40.0F;
		int blackCount = 0;
		timer.scheduleAtFixedRate(timerTask, 0, 4);

		Delay.msDelay(1000);//１秒たってから黒線検知を始める

		while(blackCount < 1){
			if(briMeasure.getNormalizedBrightness() < 0.35F){
				blackCount++;
			}
			//黒線に達したらライントレースを切る
		}
		Delay.msDelay(250);
		isLinetrace = false;
		distMeasure.resetDistance();

		//ガレージのほうを向く
		dACtrl.turn(-65.0F, false);

		//ガレージに入る
		dACtrl.goStraightAhead(LINE_TO_GARAGE_DISTANCE, 50.0F);

		//方向転換する
		dACtrl.turn(40.0F, false);
	}

}
