package sectionRun;

import driveControl.DistanceAngleController;
import lejos.utility.Delay;
import virtualDevices.ArmController;

/**
 * 懸賞を下ろす
 * ライン上にいる状態から、90度回転して懸賞を置いて、ラインの方を向く
 * */
public class PutPrize extends SectionRun {

	ArmController armCtrl = new ArmController();
	DistanceAngleController dACtrl = new DistanceAngleController();
	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		dACtrl.turn(90.0F, false);
		dACtrl.goStraightAhead(7.0F, 45.0F);
		armCtrl.controlArmPutAngel();
		Delay.msDelay(400);
		dACtrl.goStraightAhead(-10.0F, 55.0F);
		//dACtrl.turn(180.0F, false);
		armCtrl.controlArmNormalAngel();
		Delay.msDelay(100);

	}

}
