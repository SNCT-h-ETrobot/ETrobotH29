package UserInterface;

import lejos.hardware.lcd.LCD;
import virtualDevices.ArmController;
import virtualDevices.BrightnessMeasure;
//import virtualDevices.Communicator;
import virtualDevices.DistanceMeasure;
import virtualDevices.MotorAngleMeasure;
import Hardware.Hardware;
//import Information.BlockArrangeInfo;
//import Information.RouteDriver;
import driveInstruction.Controller;

public class Starter {

	private Controller controller;
	private DistanceMeasure disMeasure;
	private BrightnessMeasure briMeasure;
	private MotorAngleMeasure angMeasure;
	private ArmController armCtrl;
//	private Communicator com;
//	private RouteDriver routeDriver;

	public Starter(){
		controller = new Controller();
		disMeasure = new DistanceMeasure();
		briMeasure = new BrightnessMeasure();
		angMeasure = new MotorAngleMeasure();
		armCtrl = new ArmController();
//		com = new Communicator();
//		routeDriver = new RouteDriver();

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
	/*
		LCD.drawString("Connect Ready", 0, 0);
		BlockArrangeInfo.makeConnection();
		LCD.drawString("Connect OK   ", 0, 0);
		com.establishConnection();
		int code = com.readCode();
		BlockArrangeInfo.setBlockPlace(code);
		routeDriver.driveRoute();
	*/
	}
	public void touchStart(){
		armCtrl.controlArmNormalAngel();
		float[] sampleTouch = new float[Hardware.touch.sampleSize()];
		LCD.drawString("Touch Ready", 0, 0);
		while(true){
			Hardware.touchMode.fetchSample(sampleTouch, 0);
			if(sampleTouch[0] != 0){
				break;
			}
		}
		LCD.drawString("Start      ", 0, 0);

		controller.drive();
	}
}
