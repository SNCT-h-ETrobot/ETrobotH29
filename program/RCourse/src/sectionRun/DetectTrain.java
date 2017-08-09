package sectionRun;

import lejos.utility.Delay;
import Hardware.Hardware;

/**
 * 新幹線を検知する<br>
 * レール前で静止している状態から、新幹線を検知して、新幹線が前にいないことを確認するまで待機する
 * */
public class DetectTrain extends SectionRun {
	private static long passedTime;
	private int detectID;

	private static final float DISTANCE_NEAR = 10.0f;
	//private static final float DISTANCE_FAR = 30.0f; //2番目の検知の時で新幹線が奥にいる時に使う予定
	private static final int STOP_TIME_MS = 1000;

	private static final float TRAIN_TIME_CYCLE = 10.0f;
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
			while(true){
				Delay.msDelay(4);
				Hardware.gyro.fetchSample(sample, 0);
				if(sample[0] < DISTANCE_NEAR){
					break;
				}
			}
			// 新幹線が通り過ぎたことを検知
			while(true){
				Delay.msDelay(4);
				Hardware.gyro.fetchSample(sample, 0);
				if(sample[0] > DISTANCE_NEAR){
					break;
				}
			}
			passedTime = System.nanoTime();
			// ちょっと待つ
			Delay.msDelay(STOP_TIME_MS);
		}


		// 時間検知
		else{
			while(true){
				//新幹線が奥のレールに近いほど1に近くなるつもり
				double a = Math.cos(((System.nanoTime()-passedTime)/TRAIN_TIME_CYCLE-TRAIN_TIME_DISTANCE)*2*Math.PI);
				if(0.5>a)break;
			}

		}
	}

}
