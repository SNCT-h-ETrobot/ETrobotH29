package sectionRun;

import driveControl.DistanceAngleController;
import lejos.utility.Delay;
import virtualDevices.ArmController;
import virtualDevices.HSVColorDetector;

/**
 * ブロックを移動する
 * ブロックから一定距離にいる状態から、ブロック色検知をして押し出しか寄り切りを行い、ブロック置き場から一定距離・逆方向を向いて停止する
 * */
public class MoveBlock extends SectionRun {

	private int placeColor;
	private boolean spin;
	private boolean push;
	DistanceAngleController dACtrl;
	ArmController armCtrl;
	HSVColorDetector colorDetector;

	//走行速度
	private static final float RUN_SPEED = 60.0F;
	//色検知するときのブロックまでの距離
	private static final float COLOR_DETECTION_DISTANCE = 5.0F;
	//寄り切りに必要な移動距離（ブロックの半径＋白円の半径）
	private static final float SAME_COLOR_DISTANCE = 6.0F;

	public MoveBlock(int placeColor, boolean spin, boolean push) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.placeColor = placeColor;
		this.spin = spin;
		this.push = push;
		dACtrl = new DistanceAngleController();
		armCtrl = new ArmController();
		colorDetector = new HSVColorDetector();
	}

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ


		if(placeColor == colorDetector.getColorID())
		{
			PushOutSameColor();
		}
		else
		{
			PushOutDifferenceColor();
		}


	}

	//寄り切り
	private void PushOutSameColor()
	{

		if(push){
			//押し出し式寄り切り
			//前進
			dACtrl.goStraightAhead((COLOR_DETECTION_DISTANCE+SAME_COLOR_DISTANCE)*0.5F, RUN_SPEED);
			//後退
			//dACtrl.goStraightAhead((-SAME_COLOR_DISTANCE-COLOR_DETECTION_DISTANCE)*0.4F, RUN_SPEED);
			//180°回転
			//turnHalf(false);
			float a = spin ? 1 : -1;
			dACtrl.turn(-45.0F*a, false);
			armCtrl.controlArmDetectAngel();
			Delay.msDelay(200);
			//dACtrl.turn(45.0F*a, false);
			//dACtrl.goStraightAhead((-SAME_COLOR_DISTANCE-COLOR_DETECTION_DISTANCE)*0.3F, RUN_SPEED);
			dACtrl.turn(-135.0F*a, false);
			armCtrl.controlArmNormalAngel();
			Delay.msDelay(200);
		}
		else{
			//回転式寄り切り
			dACtrl.goStraightAhead((COLOR_DETECTION_DISTANCE+SAME_COLOR_DISTANCE) * 0.5F, RUN_SPEED);
			turnHalf(true);
			//dACtrl.goStraightAhead((COLOR_DETECTION_DISTANCE+SAME_COLOR_DISTANCE) * 0.4F, RUN_SPEED);
			//dACtrl.turn(180.0F, false);
		}
	}

	//押し出し
	private void PushOutDifferenceColor()
	{
		//前進
		dACtrl.goStraightAhead((COLOR_DETECTION_DISTANCE+SAME_COLOR_DISTANCE)*1.0f, RUN_SPEED);
		//押し出し
		armCtrl.controlArmDetectAngel();
		Delay.msDelay(300);
		armCtrl.controlArmNormalAngel();
		Delay.msDelay(300);
		dACtrl.goStraightAhead((-SAME_COLOR_DISTANCE-COLOR_DETECTION_DISTANCE)*0.5F, RUN_SPEED);
		//180°回転
		turnHalf(false);
	}

	//半回転 内側に回転する時true
	private void turnHalf(boolean turnInside){
		if(spin^turnInside)
		{
			dACtrl.turn(-180.0F, false);
		}
		else
		{
			dACtrl.turn(180.0F, false);
		}
	}

}
