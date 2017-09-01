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
	// 色検知の時にアームがぶつかっちゃうみたいなので考慮してください

	int placeColor;
	boolean spin;
	DistanceAngleController dACtrl;
	ArmController armCtrl;
	HSVColorDetector colorDetector;
	
	//走行速度
	final float RUN_SPEED = 60.0F;
	//色検知するときのブロックまでの距離
	final float COLOR_DETECTION_DISTANCE = 5.0F;
	//寄り切りに必要な移動距離（ブロックの半径＋白円の半径）
	final float SAME_COLOR_DISTANCE = 6.0F;
	
	public MoveBlock(int placeColor , boolean spin) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.placeColor = placeColor;
		this.spin = spin;
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
		//寄り切り
		dACtrl.goStraightAhead((COLOR_DETECTION_DISTANCE+SAME_COLOR_DISTANCE) * 0.4F, RUN_SPEED);
		if(spin)
		{
			dACtrl.turn(180.0F, false);
		}
		else
		{
			dACtrl.turn(-180.0F, false);
		}
		dACtrl.goStraightAhead((COLOR_DETECTION_DISTANCE+SAME_COLOR_DISTANCE) * 0.4F, RUN_SPEED);
		dACtrl.turn(180.0F, false);
	}
	
	//押し出し
	private void PushOutDifferenceColor()
	{
		//前進
		dACtrl.goStraightAhead(COLOR_DETECTION_DISTANCE+SAME_COLOR_DISTANCE, RUN_SPEED);
		//押し出し
		armCtrl.controlArmDetectAngel();
		Delay.msDelay(500);
		armCtrl.controlArmNormalAngel();
		Delay.msDelay(500);
		dACtrl.goStraightAhead(-SAME_COLOR_DISTANCE-COLOR_DETECTION_DISTANCE, RUN_SPEED);
		//180°回転
		//dACtrl.turn(180.0F, false);
	}

}
