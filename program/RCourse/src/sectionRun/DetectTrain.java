package sectionRun;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import virtualDevices.BrightnessMeasure;
import Hardware.Hardware;

/**
 * 新幹線を検知する<br>
 * レール前で静止している状態から、新幹線を検知して、新幹線が前にいないことを確認するまで待機する
 * */
public class DetectTrain extends SectionRun {
	private static long passedTime;
	private int detectID;
	private static boolean detectFar = false;

	private static final float DISTANCE_NEAR = 0.30f;
	private static final float DISTANCE_FAR = 0.90f; //2番目の検知の時に新幹線が奥にいるかの判定に使う
	private static final int STOP_TIME_MS = 1000;

	private static final float TRAIN_TIME_CYCLE = 27.0f;//一周する時間
	private static final float TRAIN_TIME_DIFFERENCE = 0.55f;//新幹線が中央レーンから奥のレーンに移動するまでの時間。一周する時間を1とした割合で
	private static final float TRAIN_TIME_NONE = 0.05f;//

	BrightnessMeasure bright = new BrightnessMeasure();

	// 引数ををbooleanからintに変えました
	//
	public DetectTrain(int detectID) {
		this.detectID = detectID;
	}

	@Override
	public void run() {
		// 距離検知
		if(detectID!=2){
			float[] sample = new float[Hardware.distanceMode.sampleSize()];
			// 新幹線がいることを検知
			Hardware.distanceMode.fetchSample(sample, 0);
			int counter = 0;
			while(true){
				LCD.drawString("ready to detect:"+counter, 0, 1);
				//Delay.msDelay(4);
				Hardware.distanceMode.fetchSample(sample, 0);
				if(sample[0] < DISTANCE_NEAR){
					if(++counter > 0){
						detectFar = false;
						break;
					}
				}
				else if(detectID==1 && sample[0] < DISTANCE_FAR){
					if(++counter > 0){
						detectFar = true;
						break;
					}
				}
				else counter = 0;
				LCD.drawString("distance:"+sample[0], 0, 2);
			}
			counter = 0;
			// 新幹線が通り過ぎたことを検知
			while(true){
				LCD.drawString("detected       "+counter, 0, 1);
				Delay.msDelay(4);
				Hardware.distanceMode.fetchSample(sample, 0);
				if(!detectFar && sample[0] > DISTANCE_NEAR){
					if(++counter > 1)break;
				}
				else if(detectFar && sample[0] > DISTANCE_FAR){
					if(++counter > 1)break;
				}
				else counter = 0;
			}
			passedTime = System.nanoTime();
			// ちょっと待つ
			Delay.msDelay(STOP_TIME_MS);
			if(detectID!=1) bright.changeLocation();
				
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
