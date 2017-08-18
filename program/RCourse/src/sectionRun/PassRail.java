package sectionRun;

import java.util.Timer;
import java.util.TimerTask;

import Hardware.Hardware;
import lejos.utility.Delay;
import virtualDevices.DistanceMeasure;
import driveControl.DistanceAngleController;
import driveControl.Linetracer;

/**
 * レールを超える
 * レール前で静止している状態から、レール上を直進し、ライン復帰する
 * */
public class PassRail extends SectionRun {
	private static final float TARGET_DISTANCE = 50.0f;//レールを超えるために直進する距離
	private static final float TARGET_DISTANCE_LT = 65.0f;//レール前からライン復帰後までの距離・最終的にこれだけ進む
	private static final float TARGET_SPEED = 40.0f;
	private static final float TARGET_SPEED_HI = 80.0f;

	private static final float LT_P = 50.0f;
	private static final float LT_I = 0.0f;
	private static final float LT_D = 0.0f;
	private static final float LT_BRIGHT = 0.5f;//正規化前提

	private DistanceAngleController DAC;
	private DistanceMeasure disMeasure;
	private Linetracer lt;

	public PassRail() {
		DAC = new DistanceAngleController();
		disMeasure = new DistanceMeasure();
		lt = new Linetracer();
	}

	@Override
	public void run() {
		//直進
		disMeasure.resetDistance();
		DAC.GoStraightAhead(TARGET_DISTANCE, TARGET_SPEED_HI);

		//少し右方向に進んでライン復帰準備
		DAC.Turn(20, false);
		DAC.GoStraightAhead(10, TARGET_SPEED);

		// 一定距離進むまでライントレースしてライン復帰
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask(){
			public void run(){
				lt.linetrace(LT_P, LT_I, LT_D, LT_BRIGHT, TARGET_SPEED);
			}
		};

		timer.scheduleAtFixedRate(timerTask, 0, 4);

		while(disMeasure.getDistance() < TARGET_DISTANCE_LT){
			Delay.msDelay(4);
		}
		timer.cancel();

		for (int i = 0; i < 10; i++) {
			Hardware.motorPortL.controlMotor(0, 1);
			Hardware.motorPortR.controlMotor(0, 1);
			Delay.msDelay(4);
		}

	}

}
