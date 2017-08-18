package sectionRun;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import Hardware.Hardware;

/**
 * 新幹線を検知する<br>
 * レール前で静止している状態から、新幹線を検知して、新幹線が前にいないことを確認するまで待機する
 * */
public class DetectTrain extends SectionRun {
	private static long passedTime;
	private int detectID;
	private static boolean detectFar = false;

	private static final float DISTANCE_NEAR = 5.0f;
	private static final float DISTANCE_FAR = 40.0f; //2番目の検知の時に新幹線が奥にいるかの判定に使う
	private static final int STOP_TIME_MS = 1000;

	private static final float TRAIN_TIME_CYCLE = 10.0f;//一周する時間
	private static final float TRAIN_TIME_DISTANCE = 0.45f;//新幹線が中央レーンから奥のレーンに移動するまでの時間。一周する時間を1とした割合で

	// 引数ををbooleanからintに変えました
	//
	public DetectTrain(int detectID) {
		this.detectID = detectID;
	}

	@Override
	public void run() {
		// 距離検知
		if(detectID!=2){
			float[] sample = new float[Hardware.gyro.sampleSize()];
			// 新幹線がいることを検知
			int counter = 0;
			while(true){
				LCD.drawString("ready to detect"+counter, 0, 1);
				Delay.msDelay(4);
				Hardware.gyro.fetchSample(sample, 0);
				if(sample[0] < DISTANCE_NEAR){
					if(++counter > 3){
						detectFar = false;
						break;
					}
				}
				else if(detectID==1 && sample[0] < DISTANCE_FAR && ++counter > 3){
					if(++counter > 3){
						detectFar = true;
						break;
					}
				}
				else counter = 0;
			}
			counter = 0;
			// 新幹線が通り過ぎたことを検知
			while(true){
				LCD.drawString("detected       "+counter, 0, 1);
				Delay.msDelay(4);
				Hardware.gyro.fetchSample(sample, 0);
				if(!detectFar && sample[0] > DISTANCE_NEAR){
					if(++counter > 3)break;
				}
				else if(detectFar && sample[0] > DISTANCE_FAR){
					if(++counter > 3)break;
				}
				else counter = 0;
			}
			passedTime = System.nanoTime();
			// ちょっと待つ
			Delay.msDelay(STOP_TIME_MS);
		}


		// 時間検知
		else{
			while(true){
				//新幹線が奥のレールに近いほど1に近くなるつもり
				float timeDiff = detectFar ? 0 : TRAIN_TIME_DISTANCE;
				double a = Math.cos(((System.nanoTime()-passedTime)/TRAIN_TIME_CYCLE-timeDiff)*2*Math.PI);
				if(0.5>a)break;
			}

		}
	}

}
