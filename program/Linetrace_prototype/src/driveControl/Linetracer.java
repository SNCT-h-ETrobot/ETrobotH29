package driveControl;

import virtualDevices.BrightnessMeasure;

public class Linetracer {

	private BrightnessMeasure briMeasure;
	private WheelController wheelCtrl;

	private float currentDiff;
	private float preDiff;

	private final float DELTA_T = 0.004F;

	public Linetracer(){
		briMeasure = new BrightnessMeasure();
		wheelCtrl = new WheelController();
		currentDiff = 0.0F;
		preDiff = 0.0F;
	}

	 public void linetrace(float kp,float ki,float kd,float target_brightness,float target_forward){
		float brightness = briMeasure.getBrightness();

		currentDiff = brightness - target_brightness;

		float turn = kp * currentDiff;

		turn += kp * (this.currentDiff - preDiff)/DELTA_T;
		turn += kp * ((currentDiff + preDiff) / 2.0F)*DELTA_T;

		preDiff = currentDiff;

		wheelCtrl.controlWheels(turn,target_forward);
	}
}
