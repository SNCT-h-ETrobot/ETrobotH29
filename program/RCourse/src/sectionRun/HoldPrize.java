package sectionRun;

import driveControl.DistanceAngleController;
import lejos.hardware.Sound;
import lejos.utility.Delay;
import virtualDevices.ArmController;
import virtualDevices.USDistanceMeasure;

/**
 * 懸賞を持つ
 * 懸賞から一定距離の状態から、懸賞を持って、元の位置に戻る
 * */
public class HoldPrize extends SectionRun {

	ArmController armCtrl = new ArmController();
	DistanceAngleController dACtrl = new DistanceAngleController();
	USDistanceMeasure uSDisMeasure = new USDistanceMeasure();

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		/*
		armCtrl.controlArmPutAngel();
		dACtrl.goStraightAhead(uSDisMeasure.getDistance() * 100.0F, 40);
		armCtrl.controlArmHoldAngel();
		Delay.msDelay(800);
		//180°回転
		dACtrl.turn(180.0F, false);
		*/
		dACtrl.turn(360.0F, false);


		for(int i = 0; i < 4; ++i){
			Sound.playTone(1500, 100);
			armCtrl.controlArmHoldAngel();
			Delay.msDelay(200);
			Sound.playTone(1000, 100);
			armCtrl.controlArmNormalAngel();
			Delay.msDelay(200);
		}

		while(true){
			Delay.msDelay(1000);
		}
		//前進
		//dACtrl.goStraightAhead(10.0F, 40);

	}

}
