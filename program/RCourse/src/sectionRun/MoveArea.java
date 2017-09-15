package sectionRun;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import virtualDevices.BrightnessMeasure;
import virtualDevices.DistanceMeasure;
import virtualDevices.MotorAngleMeasure;
import driveControl.DistanceAngleController;
import driveControl.Linetracer;
import driveControl.WheelController;

/**
 * エリア内を移動する
 * エリア内のある地点から、別のある地点に移動し、停止する
 * */
public class MoveArea extends SectionRun {
	private static final float BLOCK_DISTANCE = 10.0f;//中央からブロックの目標地点までの距離
	private static final float BLOCK_PASS_DISTANCE = 30.0f;//中央からブロックの目標地点までの距離
	private static final float CENTER_DISTANCE = 14.0f;//ブロックから中央までの距離
	private static final float PRIZE_DISTANCE = 20.0f;//中央から懸賞の目標地点までの距離
	private static final float RAIL_DISTANCE = 13.0f;//レールに近い方の縦ラインからレールまで進む距離
	private static final float RAIL_CENTER_DISTANCE = 12.0f;//中央からレールまで進む距離

	private static final float TARGET_SPEED = 60.0f;
	private static final float TARGET_SPEED_LO = 40.0f;

	private static final float LT_P = 120.0f;
	private static final float LT_I = 30.0f;
	private static final float LT_D = 10.0f;
	private static final float LT_P_LO = 60.0f;
	private static final float LT_P_RIGHT = 160.0f;
	private static final float LT_I_RIGHT = 10.0f;
	private static final float LT_D_RIGHT = 5.0f;

	private static final float LT_BRIGHT = 0.5f;//正規化前提

	private static final float RIGHT_ANGLE_THRESHOLD = 200.0f;
	private static final float BLACK_THRESHOLD = 0.15f;//黒ライン検知の色しきい値
	private static final float RIGHT_TURN_ANGLE = 90;//直角検知の時に曲がる角度
	private static final float RIGHT_TURN_ANGLE_INVERSE = 95;//直角検知の時に曲がる角度

	private float ltp,lti,ltd,ltb,lts;
	private boolean useLT = false;
	enum traceEdge {RIGHT, LEFT, UNKNOWN}

	private DistanceAngleController dac;
	private DistanceMeasure dm;
	private Linetracer lt;
	private BrightnessMeasure bm;
	private WheelController wc;
	private MotorAngleMeasure mam;

	// 動作のの組み合わせで表現する
	enum actionType { TURN_HALF,       /*180度回転*/
	                  STRAIGHT,        /*ブロック前から反対のブロック前まで直進*/
	                  CORNER_TO_RIGHT, /*ブロック前から右に曲がって中央ラインへ*/
	                  CORNER_TO_LEFT,  /*ブロック前から左に曲がって中央ラインへ*/
	                  CENTER_TO_RIGHT, /*中央ラインから右に曲がってブロック前へ*/
	                  CENTER_TO_LEFT,  /*中央ラインから左に曲がってブロック前へ*/
	                  CORNER_TO_RAIL,  /*ブロック前から右に曲がってレール前へ*/
	                  CORNER_TO_PRIZE, /*中央ラインから懸賞前へ*/
	                  PRIZE_TO_RAIL,   /*懸賞前からレール前へ*/
	                  }
	private ArrayList<actionType> actionList;

	public MoveArea(int areaID) {
		dac = new DistanceAngleController();
		dm = new DistanceMeasure();
		lt = new Linetracer();
		bm = new BrightnessMeasure();
		wc = new WheelController();
		mam = new MotorAngleMeasure();
		actionList = new ArrayList<>();

		switch(areaID){
		case 1:
			actionList.add(actionType.TURN_HALF);
			actionList.add(actionType.CENTER_TO_LEFT);
			break;
		case 2:
			//actionList.add(actionType.TURN_HALF);
			actionList.add(actionType.STRAIGHT);
			break;
		case 3:
			//actionList.add(actionType.TURN_HALF);
			actionList.add(actionType.CORNER_TO_LEFT);
			actionList.add(actionType.CENTER_TO_LEFT);
			break;
		case 4:
			//actionList.add(actionType.TURN_HALF);
			actionList.add(actionType.STRAIGHT);
			break;
		case 5:
			//actionList.add(actionType.TURN_HALF);
			actionList.add(actionType.CORNER_TO_RAIL);
			break;
		case 6:
			//actionList.add(actionType.TURN_HALF);
			//actionList.add(actionType.CORNER_TO_LEFT);
			actionList.add(actionType.CORNER_TO_PRIZE);
			break;
		case 7:
			actionList.add(actionType.PRIZE_TO_RAIL);
		}
	}


	@Override
	public void run() {


		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask(){
			public void run(){
				if(useLT)lt.linetrace(ltp,lti,ltd,ltb,lts);
			}
		};

		timer.scheduleAtFixedRate(timerTask, 0, 4);

		traceEdge edge = traceEdge.UNKNOWN;
		for(int i = 0; i < actionList.size();i++){
			//区間の距離と走行した距離を比較
			switch(actionList.get(i)){
			case TURN_HALF:
				turnHalf();
				edge = traceEdge.UNKNOWN;
				break;
			case STRAIGHT:
				straight(!(edge == traceEdge.RIGHT));
				edge = traceEdge.RIGHT;
				break;
			case CORNER_TO_RIGHT:
				cornerToRight(!(edge == traceEdge.RIGHT));
				edge = traceEdge.RIGHT;
				break;
			case CORNER_TO_LEFT:
				cornerToLeft(!(edge == traceEdge.LEFT));
				edge = traceEdge.LEFT;
				break;
			case CENTER_TO_RIGHT:
				centerToRight(!(edge == traceEdge.RIGHT));
				edge = traceEdge.RIGHT;
				break;
			case CENTER_TO_LEFT:
				centerToLeft(!(edge == traceEdge.LEFT));
				edge = traceEdge.LEFT;
				break;
			case CORNER_TO_RAIL:
				cornerToRail();
				edge = traceEdge.RIGHT;
				break;
			case CORNER_TO_PRIZE:
				cornerToPrize();
				edge = traceEdge.UNKNOWN;
				break;
			case PRIZE_TO_RAIL:
				prizeToRail();
				edge = traceEdge.RIGHT;
				break;
			default:
				break;
			}

		}

		timer.cancel();

		stop();
		/*for (int i = 0; i < 10; i++) {
			Hardware.motorPortL.controlMotor(0, 1);
			Hardware.motorPortR.controlMotor(0, 1);
			Delay.msDelay(4);
		}*/


	}

	public void turnHalf(){
		useLT = false;
		dac.turn(180, false);
		stop();
	}


	public void straight(boolean Inverse){
		if(Inverse){
			useLT = false;
			dac.turn(10, false);
		}
		// LTする->LT係数低くして中央の黒ラインを突っ切る->係数元に戻してLTする
		useLT = true;
		dm.resetDistance();
		ltp = LT_P;
		lti = LT_I;
		ltd = LT_D;
		ltb = LT_BRIGHT;
		lts = TARGET_SPEED_LO;
		while(dm.getDistance() < BLOCK_PASS_DISTANCE*0.3){
			Delay.msDelay(4);
		}
		ltp = LT_P_LO;
		lti = 0;
		ltd = 0;
		lts = TARGET_SPEED;
		while(dm.getDistance() < BLOCK_PASS_DISTANCE*0.7){
			Delay.msDelay(4);
		}
		ltp = LT_P;
		lti = LT_I;
		ltd = LT_D;
		lts = TARGET_SPEED_LO;
		while(dm.getDistance() < BLOCK_PASS_DISTANCE){
			Delay.msDelay(4);
		}
	}

	public void cornerToRight(boolean Inverse){
		if(Inverse){
			useLT = false;
			dac.turn(10, false);
		}
		useLT = true;
		detectRightAngle(true);
		dm.resetDistance();
		ltp = LT_P;
		lti = LT_I;
		ltd = LT_D;
		lts = TARGET_SPEED;
		while(dm.getDistance() < CENTER_DISTANCE){
			Delay.msDelay(4);
		}
	}

	public void cornerToLeft(boolean Inverse){
		if(Inverse){
			useLT = false;
			dac.turn(-10, false);
		}
		useLT = true;
		detectRightAngle(false);
		dm.resetDistance();
		ltp = -LT_P;
		lti = -LT_I;
		ltd = -LT_D;
		lts = TARGET_SPEED;
		while(dm.getDistance() < CENTER_DISTANCE){
			Delay.msDelay(4);
		}

	}

	public void centerToRight(boolean Inverse){
		if(Inverse){
			useLT = false;
			dac.turn(10, false);
		}
		useLT = true;
		detectRightAngle(true);
		dm.resetDistance();
		ltp = LT_P;
		lti = LT_I;
		ltd = LT_D;
		lts = TARGET_SPEED;
		while(dm.getDistance() < BLOCK_DISTANCE){
			Delay.msDelay(4);
		}
		stop();
	}

	public void centerToLeft(boolean Inverse){
		if(Inverse){
			useLT = false;
			dac.turn(-10, false);
		}
		useLT = true;
		detectRightAngle(false);
		dm.resetDistance();
		ltp = -LT_P;
		lti = -LT_I;
		ltd = -LT_D;
		lts = TARGET_SPEED;
		while(dm.getDistance() < BLOCK_DISTANCE){
			Delay.msDelay(4);
		}
		stop();
	}

	public void cornerToRail(){
		//useLT = false;
		//dac.turn(40, false);
		//detectBlackLine(false);
		useLT = true;
		detectRightAngle(false, true);
		useLT = false;
		dm.resetDistance();
		dac.goStraightAhead(-15, 40);//後退してライントレース距離を確保
		//dac.turn(-10, false);

		useLT = true;
		dm.resetDistance();
		ltp = LT_P;
		lti = LT_I;
		ltd = LT_D;
		ltb = LT_BRIGHT;
		lts = TARGET_SPEED_LO;
		while(dm.getDistance() < RAIL_DISTANCE/2){
			Delay.msDelay(4);
		}/*
		ltp = LT_P_LO;
		lti = 0;
		ltd = 0;
		lts = TARGET_SPEED;
		while(dm.getDistance() < 0){
			Delay.msDelay(4);
		}*/
		ltp = LT_P_LO;
		lti = 0;
		ltd = 0;
		lts = TARGET_SPEED_LO;
		while(dm.getDistance() < RAIL_DISTANCE){
			Delay.msDelay(4);
		}

		/*useLT = true;
		ltp = LT_P;
		lti = LT_I;
		ltd = LT_D;
		lts = TARGET_SPEED_LO;
		while(dm.getDistance() < RAIL_DISTANCE){
			Delay.msDelay(4);
		}*/
		stop();
	}

	//
	public void cornerToPrize(){
		useLT = false;
		dac.turn(-10, false);
		useLT = true;
		detectRightAngle(false);
		dm.resetDistance();
		ltp = -LT_P;
		lti = -LT_I;
		ltd = -LT_D;
		lts = TARGET_SPEED_LO;
		while(dm.getDistance() < CENTER_DISTANCE){
			Delay.msDelay(4);
		}

		useLT = false;
		stop();
		dac.turn(-90, false);
		stop();
		//dac.goStraightAhead(PRIZE_DISTANCE, 40);
		//stop();
	}

	private void prizeToRail(){
		useLT = false;
		dac.goStraightAhead(PRIZE_DISTANCE, 40);
		stop();
		dac.turn(90, false);
		stop();
		//dac.goStraightAhead(RAIL_CENTER_DISTANCE, 40);
		//stop();
		/*dac.turn(105, false);//大きめにしてLTで戻す
		stop();
		dm.resetDistance();
		ltp = LT_P;
		lti = LT_I;
		ltd = LT_D;
		ltb = LT_BRIGHT;
		lts = TARGET_SPEED_LO;
		while(dm.getDistance() < RAIL_DISTANCE + 15){
			Delay.msDelay(4);
		}*/
	}


	/* 直角を検知する RightAngleは直角のこと
	 * 引数のisTraceRightはラインの右側をトレースするかどうか, isTurnInverseはトレースした側と逆に回転するかどうか
	 * ライントレースで用いているcontrolWheelsでは最大回転ても片輪が停止するまでのため最小旋回半径の関係でラインを直角に曲がりきることができない
	 * そのため黒色検知してDistanceAngleControllerで回転する
	 * */
	private void detectRightAngle(boolean isTraceRight){
		detectRightAngle(isTraceRight, false);
	}

	private void detectRightAngle(boolean isTraceRight, boolean isTurnInverse){
		useLT = true;
		dm.resetDistance();
		if(isTraceRight){
			ltp = LT_P;
			lti = LT_I;
			ltd = LT_D;
		}
		else {
			ltp = -LT_P;
			lti = -LT_I;
			ltd = -LT_D;
		}
		ltb = LT_BRIGHT;
		lts = TARGET_SPEED_LO;
		while(dm.getDistance() < 10){
			Delay.msDelay(4);
		}
		if(isTraceRight){
			ltp = LT_P_RIGHT;
			lti = LT_I_RIGHT;
			ltd = LT_D_RIGHT;
		}
		else{
			ltp = -LT_P_RIGHT;
			lti = -LT_I_RIGHT;
			ltd = -LT_D_RIGHT;
		}
		lts = TARGET_SPEED_LO;
		int blackCount = 0;
		while(true){
			if(bm.getNormalizedBrightness() < BLACK_THRESHOLD)++blackCount;
			else blackCount = 0;
			if(30 < blackCount)break;
			Delay.msDelay(4);
		}
		/*過去仕様
		mam.resetMotorAngle();
		int[] motorAngle = mam.getMotorAngle();
		while(true){
			motorAngle = mam.getMotorAngle();
			int  angleDiff = motorAngle[0] - motorAngle[1];
			if(angleDiff > RIGHT_ANGLE_THRESHOLD || angleDiff < -RIGHT_ANGLE_THRESHOLD)
				break;
			Delay.msDelay(4);
		}*/
		stop();
		if(isTurnInverse){
			dac.goStraightAhead(2, 40);
			stop();
			if(isTraceRight)dac.turn(-RIGHT_TURN_ANGLE_INVERSE, false);
			else dac.turn(RIGHT_TURN_ANGLE_INVERSE, false);
		}
		else{
			dac.goStraightAhead(4, 40);
			stop();
			if(isTraceRight)dac.turn(RIGHT_TURN_ANGLE, false);
			else dac.turn(-RIGHT_TURN_ANGLE, false);
		}
		stop();
		//Delay.msDelay(4000);
		useLT = true;
	}

	/* 回転して黒ラインを見つける*/
	private void detectBlackLine(boolean isTurnRight){
		useLT=false;
		if(isTurnRight)wc.controlWheelsDirect(10, -10);
		else wc.controlWheelsDirect(-10, 10);
		int blackCount = 0;
		while(true){
			if(bm.getNormalizedBrightness() < BLACK_THRESHOLD)++blackCount;
			else blackCount = 0;
			if(10 < blackCount)break;
			Delay.msDelay(4);
		}
		stop();
	}

	private void stop(){
		useLT = false;
		for (int i = 0; i < 10; i++) {
			wc.controlWheelsDirect(0, 0);
			Delay.msDelay(4);
		}
	}


}
