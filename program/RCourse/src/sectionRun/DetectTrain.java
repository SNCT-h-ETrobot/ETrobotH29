package sectionRun;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import virtualDevices.BrightnessMeasure;
import virtualDevices.USDistanceMeasure;

/**
 * 新幹線を検知する<br>
 * レール前で静止している状態から、新幹線を検知して、新幹線が前にいないことを確認するまで待機する
 * */
public class DetectTrain extends SectionRun {
	private static long passedTime;
	private int detectID;
	private static boolean detectFar = false;

	private static final float DISTANCE_NEAR = 0.20f;
	private static final float DISTANCE_FAR = 0.90f; //2番目の検知の時に新幹線が奥にいるかの判定に使う
	private static final int STOP_TIME_MS = 1000;

	private static final float TRAIN_TIME_CYCLE = 19.5f;//一周する時間
	private static final float TRAIN_TIME_DIFFERENCE = 0.55f;//新幹線が中央レーンから奥のレーンに移動するまでの時間。一周する時間を1とした割合で
	private static final float TRAIN_TIME_NONE = 0.05f;//

	BrightnessMeasure bright = new BrightnessMeasure();
	USDistanceMeasure usdistance = new USDistanceMeasure();


	// 引数ををbooleanからintに変えました
	//
	public DetectTrain(int detectID) {
		this.detectID = detectID;
	}

	@Override
	public void run() {
		// 距離検知
		if(detectID!=2){
			// 新幹線がいることを検知
			float distance;
			int counter = 0;
			LCD.clear();
			LCD.drawString("ready to detect:"+counter, 0, 1);
			while(true){
				distance = usdistance.getDistance();
				//Delay.msDelay(4);
				if(distance < DISTANCE_NEAR){
					detectFar = false;
					break;
				}
				else if(detectID==1 && distance < DISTANCE_FAR){
					detectFar = true;
					break;
				}
				else counter = 0;
				LCD.drawString("distance:"+distance, 0, 2);
			}
			Sound.playTone(detectFar ? 2000 : 1000, 100);;
			counter = 0;
			// 新幹線が通り過ぎたことを検知
			LCD.drawString("detected       "+counter, 0, 1);
			while(true){
				distance = usdistance.getDistance();
				if(!detectFar && distance > DISTANCE_NEAR){
					break;
				}
				else if(detectFar && distance > DISTANCE_FAR){
					break;
				}
				else counter = 0;
			}
			passedTime = System.nanoTime();
			// ちょっと待つ
			Sound.playTone(1200, 100);;
			Delay.msDelay(STOP_TIME_MS);
			if(detectID==0) bright.changeLocation(false);
			else if(detectID==2) bright.changeLocation(true);
		}


		// 時間検知
		else{
			while(true){
				//新幹線が奥のレールに近いほど1に近くなるつもり
				float timeDiff = detectFar ? TRAIN_TIME_NONE : TRAIN_TIME_DIFFERENCE;
				double elapsedTime = (System.nanoTime()-passedTime)/Math.pow(10, 9);
				double a = Math.cos((elapsedTime/TRAIN_TIME_CYCLE + timeDiff)*2*Math.PI);
				LCD.drawString("cos:"+a, 0, 3);
				if(0.5>a)break;
			}

		}
	}

}
