package sectionRun;

import java.util.Timer;
import java.util.TimerTask;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import virtualDevices.BrightnessMeasure;
import virtualDevices.DistanceMeasure;
import driveControl.DistanceAngleController;
import driveControl.Linetracer;

/**
 * ガレージイン
 * ライン上にいる状態から、直角を検知して曲がってガレージに入って停止する
 * */
public class GarageIn extends SectionRun {
	private DistanceAngleController dACtrl = new DistanceAngleController();
	private Linetracer linetracer = new Linetracer();
	private DistanceMeasure distMeasure = new DistanceMeasure();
	private BrightnessMeasure briMeasure = new BrightnessMeasure();

	private final float KP = 80.0F;
	private final float KI = 30.0F;
	private final float KD = 5.0F;

	private float kp;
	private float ki;
	private float kd;
	private float targetBrightness = 0.5F;
	private float targetForward;

	private boolean isLinetrace;

	@Override
	public void run() {
		//ライントレース部（isLinetraceによって実行したりしなかったりする）
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask(){
			public void run(){
				if(isLinetrace)linetracer.linetrace(kp, ki, kd, targetBrightness, targetForward);
			}
		};

		//線を検知するまで進む
		kp = 0.0F;
		ki = 0.0F;
		kd = 0.0F;
		targetForward  = -40.0F;
		int blackCount = 0;
		timer.scheduleAtFixedRate(timerTask, 0, 4);
		Delay.msDelay(100);
		for(int i=0;i<10;i++){
			//ローパスで狂うので空実行
			briMeasure.getNormalizedBrightness();
		}
		isLinetrace = true;
		while(blackCount < 1){
			if(briMeasure.getNormalizedBrightness() < 0.35F){
				blackCount++;
			}
			//黒線に達したらライントレースを切る
		}
		distMeasure.resetDistance();
		targetForward  = 40.0F;
		while(distMeasure.getDistance()< 4.0F){
			//切る前に位置わせのため4cmオーバーランする
		}
		isLinetrace = false;

		//ラインのほうを向く
		dACtrl.turn(-90.0F, false);

		//角まで進む
		linetracer.reset();
		distMeasure.resetDistance();
		kp = KP;
		ki = KI;
		kd = KD;
		targetForward = 60.0F;
		isLinetrace = true;
		while(distMeasure.getDistance() < 36.0F){
			if(distMeasure.getDistance() > 20.0F){
				targetForward = 40.0F;
				if(briMeasure.getNormalizedBrightness() < 0.15F){
					break;
				}
			}
			LCD.clear();
			LCD.drawString("distance:"+distMeasure.getDistance(), 0, 0);
			Delay.msDelay(4);
		}
		isLinetrace = false;

		//ガレージのほうを向く
		dACtrl.turn(90.0F, false);

		//ガレージ内まで進む
		distMeasure.resetDistance();
		targetForward = 65.0F;
		isLinetrace = true;
		while(distMeasure.getDistance() < 45.0F){
			if(distMeasure.getDistance() > 35.0F){
				targetForward = 40.0F;
			}
			LCD.clear();
			LCD.drawString("distance:"+distMeasure.getDistance(), 0, 0);
			Delay.msDelay(4);
		}

		//速度を0にして停止
		targetForward = 0.0F;
		Delay.msDelay(100);
		isLinetrace = false;
		timer.cancel();

	}

}
