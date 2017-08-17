package sectionRun;

import driveControl.DistanceAngleController;
import lejos.utility.Delay;
import virtualDevices.ArmController;

/**
 * 懸賞を持つ
 * 懸賞から一定距離の状態から、懸賞を持って、元の位置に戻る
 * */
public class HoldPrize extends SectionRun {

	ArmController armCtrl = new ArmController();
	DistanceAngleController dACtrl = new DistanceAngleController();
	
	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		dACtrl.goStraightAhead(5.0F, 40);
		armCtrl.controlArmHoldAngel();
		Delay.msDelay(800);
		//180°回転
		dACtrl.turn(180.0F, false);
		//前進
		dACtrl.goStraightAhead(5.0F, 40);

	}

}
