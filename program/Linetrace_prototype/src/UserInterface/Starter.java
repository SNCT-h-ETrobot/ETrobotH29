package UserInterface;

import virtualDevices.ArmController;
import virtualDevices.BrightnessMeasure;
import virtualDevices.DistanceMeasure;
import virtualDevices.MotorAngleMeasure;
import Hardware.Hardware;
import driveInstruction.Controller;

public class Starter {

	private Controller controller;
	private DistanceMeasure disMeasure;
	private BrightnessMeasure briMeasure;
	private MotorAngleMeasure angMeasure;
	private ArmController armCtrl;

	public Starter(){
		controller = new Controller();
		disMeasure = new DistanceMeasure();
		briMeasure = new BrightnessMeasure();
		angMeasure = new MotorAngleMeasure();
		armCtrl = new ArmController();

	}

	public void calibrate(){
		//hotspotのため頻度の高い操作を1500回空実行する
		for(int i = 0; i < 1500;i++){
			disMeasure.getDistance();
			briMeasure.getBrightness();
			angMeasure.getMotorAngle();
			Hardware.motorPortL.controlMotor(0, 1);
			Hardware.motorPortR.controlMotor(0, 1);
			Hardware.motorPortA.controlMotor(0, 1);
		}
		disMeasure.resetDistance();
		angMeasure.resetMotorAngle();
		armCtrl.resetArm();
	}
	public void touchStart(){
		float[] sampleTouch = new float[Hardware.touch.sampleSize()];
		while(true){
			Hardware.touchMode.fetchSample(sampleTouch, 0);
			if(sampleTouch[0] != 0){
				break;
			}
		}

		controller.drive();
	}
}
