package driveControl;

import virtualDevices.BrightnessMeasure;

public class Linetracer {

	private BrightnessMeasure briMeasure;
	private WheelController wheelCtrl;

	private float currentDiff;
	private float preDiff;
	private float integral;

	private final float DELTA_T = 0.004F;

	public Linetracer(){
		briMeasure = new BrightnessMeasure();
		wheelCtrl = new WheelController();
		currentDiff = 0.0F;
		preDiff = 0.0F;
		integral = 0.0f;
	}

	 public void linetrace(float kp,float ki,float kd,float target_brightness,float target_forward){
		float brightness = briMeasure.getNormalizedBrightness();

		preDiff = currentDiff;
		currentDiff = brightness - target_brightness;
		integral += ((currentDiff + preDiff) / 2.0F)*DELTA_T;

		float turn = kp * currentDiff;
		turn += ki * integral;
		turn += kd * (currentDiff - preDiff)/DELTA_T;

		wheelCtrl.controlWheels(turn,target_forward);
	}
	
	 public void linetraceFast(float kp,float ki,float kd,float target_brightness,float target_forward){
		float brightness = briMeasure.getNormalizedBrightness();

		preDiff = currentDiff;
		currentDiff = brightness - target_brightness;
		integral += ((currentDiff + preDiff) / 2.0F)*DELTA_T;

		float turn = kp * currentDiff;
		turn += ki * integral;
		turn += kd * (currentDiff - preDiff)/DELTA_T;

		wheelCtrl.controlWheelsFast(turn,target_forward);
	}
	 
	 public void reset(){
		currentDiff = 0.0F;
		preDiff = 0.0F;
		integral = 0.0f;
	}

}
