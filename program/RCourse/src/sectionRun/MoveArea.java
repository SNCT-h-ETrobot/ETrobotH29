package sectionRun;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import lejos.utility.Delay;
import virtualDevices.BrightnessMeasure;
import virtualDevices.DistanceMeasure;
import virtualDevices.MotorAngleMeasure;
import Hardware.Hardware;
import driveControl.DistanceAngleController;
import driveControl.Linetracer;
import driveControl.WheelController;

/**
 * エリア内を移動する
 * エリア内のある地点から、別のある地点に移動し、停止する
 * */
public class MoveArea extends SectionRun {
	private static final float BLOCK_DISTANCE = 15.0f;//中央からブロックの目標地点までの距離
	private static final float PRIZE_DISTANCE = 25.0f;//中央から懸賞の目標地点までの距離
	private static final float RAIL_DISTANCE = 10.0f;//レールに近い方の縦ラインからレールまで進む距離

	private static final float TARGET_SPEED = 40.0f;
	private static final float TARGET_SPEED_LO = 20.0f;

	private static final float LT_P = 50.0f;
	private static final float LT_P_LO = 30.0f;
	private static final float LT_I = 0.0f;
	private static final float LT_D = 0.0f;
	private static final float LT_BRIGHT = 0.5f;//正規化前提
	private float ltp,lti,ltd,ltb,lts;
	private boolean useLT = false;
	enum traceEdge {RIGHT, LEFT, UNKNOWN}

	private DistanceAngleController DAC;
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
	                  CENTER_TO_PRIZE, /*中央ラインから懸賞前へ*/
	                  PRIZE_TO_RAIL,   /*懸賞前からレール前へ*/
	                  }
	private ArrayList<actionType> actionList;

	public MoveArea(int areaID) {
		DAC = new DistanceAngleController();
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
			actionList.add(actionType.TURN_HALF);
			actionList.add(actionType.STRAIGHT);
			break;
		case 3:
			actionList.add(actionType.TURN_HALF);
			actionList.add(actionType.CORNER_TO_LEFT);
			actionList.add(actionType.CENTER_TO_LEFT);
			break;
		case 4:
			actionList.add(actionType.TURN_HALF);
			actionList.add(actionType.STRAIGHT);
			break;
		case 5:
			actionList.add(actionType.TURN_HALF);
			actionList.add(actionType.CORNER_TO_RAIL);
			break;
		case 6:
			actionList.add(actionType.TURN_HALF);
			actionList.add(actionType.CORNER_TO_LEFT);
			actionList.add(actionType.CENTER_TO_PRIZE);
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
				straight(edge == traceEdge.RIGHT);
				edge = traceEdge.RIGHT;
				break;
			case CORNER_TO_RIGHT:
				cornerToRight(edge == traceEdge.RIGHT);
				edge = traceEdge.RIGHT;
				break;
			case CORNER_TO_LEFT:
				cornerToLeft(edge == traceEdge.LEFT);
				edge = traceEdge.LEFT;
				break;
			case CENTER_TO_RIGHT:
				centerToRight(edge == traceEdge.RIGHT);
				edge = traceEdge.RIGHT;
				break;
			case CENTER_TO_LEFT:
				centerToLeft(edge == traceEdge.LEFT);
				edge = traceEdge.LEFT;
				break;
			case CORNER_TO_RAIL:
				cornerToRail();
				edge = traceEdge.RIGHT;
				break;
			case CENTER_TO_PRIZE:
				centerToPrize();
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
		DAC.turn(180, false);
		stop();
	}


	public void straight(boolean Inverse){
		if(Inverse){
			useLT = false;
			DAC.turn(10, false);
		}
		// LTする->LT係数低くして中央の黒ラインを突っ切る->係数元に戻してLTする
		useLT = true;
		dm.resetDistance();
		ltp = LT_P;
		lti = LT_I;
		ltd = LT_D;
		ltb = LT_BRIGHT;
		lts = TARGET_SPEED_LO;
		while(dm.getDistance() < BLOCK_DISTANCE*0.4){
			Delay.msDelay(4);
		}
		ltp = LT_P_LO;
		lti = 0;
		ltd = 0;
		lts = TARGET_SPEED;
		while(dm.getDistance() < BLOCK_DISTANCE*1.4){
			Delay.msDelay(4);
		}
		ltp = LT_P;
		lti = LT_I;
		ltd = LT_D;
		lts = TARGET_SPEED_LO;
		while(dm.getDistance() < BLOCK_DISTANCE*2){
			Delay.msDelay(4);
		}
	}

	public void cornerToRight(boolean Inverse){
		if(Inverse){
			useLT = false;
			DAC.turn(10, false);
		}
		useLT = true;
		detectRightAngle(true);
		dm.resetDistance();
		lts = TARGET_SPEED;
		while(dm.getDistance() < 15){
			Delay.msDelay(4);
		}
	}

	public void cornerToLeft(boolean Inverse){
		if(Inverse){
			useLT = false;
			DAC.turn(-10, false);
		}
		useLT = true;
		detectRightAngle(false);
		dm.resetDistance();
		lts = TARGET_SPEED;
		while(dm.getDistance() < 15){
			Delay.msDelay(4);
		}

	}

	public void centerToRight(boolean Inverse){
		if(Inverse){
			useLT = false;
			DAC.turn(10, false);
		}
		useLT = true;
		detectRightAngle(true);
		dm.resetDistance();
		lts = TARGET_SPEED;
		while(dm.getDistance() < BLOCK_DISTANCE){
			Delay.msDelay(4);
		}

	}

	public void centerToLeft(boolean Inverse){
		if(Inverse){
			useLT = false;
			DAC.turn(-10, false);
		}
		useLT = true;
		detectRightAngle(false);
		dm.resetDistance();
		lts = TARGET_SPEED;
		while(dm.getDistance() < BLOCK_DISTANCE){
			Delay.msDelay(4);
		}
	}

	public void cornerToRail(){
		useLT = false;
		DAC.turn(10, false);
		useLT = true;
		detectRightAngle(true);
		dm.resetDistance();
		lts = TARGET_SPEED;
		while(dm.getDistance() < RAIL_DISTANCE){
			Delay.msDelay(4);
		}
	}

	//
	public void centerToPrize(){
		useLT = false;
		stop();
		DAC.turn(-90, false);
		stop();
		DAC.goStraightAhead(PRIZE_DISTANCE, 40);
		stop();
	}

	private void prizeToRail(){
		useLT = false;
		DAC.goStraightAhead(PRIZE_DISTANCE, 40);
		stop();
		DAC.turn(105, false);//大きめにしてLTで戻す
		stop();
		dm.resetDistance();
		ltp = LT_P;
		lti = LT_I;
		ltd = LT_D;
		ltb = LT_BRIGHT;
		lts = TARGET_SPEED_LO;
		while(dm.getDistance() < RAIL_DISTANCE + 15){
			Delay.msDelay(4);
		}
	}


	/*直角を検知する RightAngleは直角のことで、引数のisRightはラインの右側にいるかどうか*/
	private void detectRightAngle(boolean isRight){
		useLT = true;
		dm.resetDistance();
		if(isRight)ltp=LT_P;
		else ltp = -LT_P;
		lti = LT_I;
		ltd = LT_D;
		ltb = LT_BRIGHT;
		lts = TARGET_SPEED;
		while(dm.getDistance() < 5){
			Delay.msDelay(4);
		}
		lts = TARGET_SPEED_LO;
		mam.resetMotorAngle();
		int[] motorAngle = mam.getMotorAngle();
		while(true){
			motorAngle = mam.getMotorAngle();
			int  angleDiff = motorAngle[0] - motorAngle[1];
			if(angleDiff > 40 || angleDiff < -40)
				break;
			Delay.msDelay(4);
		}
	}

	private void stop(){
		useLT = false;
		for (int i = 0; i < 50; i++) {
			wc.controlWheelsDirect(0, 0);
			Delay.msDelay(4);
		}
	}


}
