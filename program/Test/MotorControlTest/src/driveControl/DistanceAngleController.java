package driveControl;

import java.util.Timer;
import java.util.TimerTask;

import lejos.hardware.lcd.LCD;
import virtualDevices.MotorAngleMeasure;

public class DistanceAngleController {
	private float targetSpeed;
	private float targetTurnAngle;//動かしたい角度(反時計回り)
	private WheelController wheelCtrl;
	private MotorAngleMeasure motorAngleMeasure;
	private boolean timerEnd;

	private final float TIRE_CIRCUMFERENCE = 25.0F;//タイヤの円周。タイヤの直径8.16cm×3.14=25.662cm
	private final float P_GAIN = 25.0F;	//距離用P係数
	private final float I_GAIN = 5.0F;	//距離用I係数
	private final float D_GAIN = 0.0F;	//距離用D係数
	private final float P_GAIN_A = 5.0F;//旋回角度用P係数
	private final float I_GAIN_A = 1.0F;//旋回角度用I係数
	private final float D_GAIN_A = 1.0F;//旋回角度用D係数
	private final float P_GAIN_S = 3.0F;//速度用P係数
	private final float I_GAIN_S = 1.0F;//速度用I係数
	private final float D_GAIN_S = 0.1F;//速度用D係数
	private final float DELTA_T = 0.004F;	//制御周期
	private final int MAX_PWM_MARGIN = 10;	//PWM値のマージン。指定スピード+これ

	public DistanceAngleController()
	{
		wheelCtrl = new WheelController();
		motorAngleMeasure = new MotorAngleMeasure();
		targetSpeed = 0.000F;
		targetTurnAngle = 0.000F;
	}

	//第一引数で移動距離、第二引数で速度
	public void goStraightAhead(float targetDistance , float speed)
	{
		targetSpeed = speed;
		motorAngleMeasure.resetMotorAngle();
		
		timerEnd = false;

		final float target = targetDistance;

		Timer timer = new Timer();
		TimerTask task = new TimerTask(){

			//この辺は可読性の為
			int angleL;	//左回転角度
			int angleR;	//右回転角度
			float angle;	//平均回転角度
			float distance;	//走行距離
			float currentDiff; //今回の距離差分
			float preDiff = 0; //以前の距離差分
			float currentDiffS; //今回の速度差分
			float preDiffS = 0; //以前の速度差分
			float pwmL;	//左輪pwm
			float pwmR;	//右輪pwm
			int i = 0;

			public void run(){
				angleL = motorAngleMeasure.getMotorAngle()[0];
				angleR = motorAngleMeasure.getMotorAngle()[1];
				angle = (float)(angleL+angleR)/2.0F;
				distance = (angle/360)*TIRE_CIRCUMFERENCE;
				currentDiff = target - distance;
				currentDiffS = (float)(angleL - angleR);

				//左輪は距離ベース
				pwmL = P_GAIN*currentDiff
						+ I_GAIN*(currentDiff-preDiff)*DELTA_T
						- D_GAIN*((currentDiff + preDiff) / 2.0F)*DELTA_T;

				//上限設定
				if(pwmL > targetSpeed){
					pwmL = targetSpeed;
				}
				else if(pwmL < -targetSpeed){
					pwmL = -targetSpeed;
				}

				//右輪は速度ベース≒左輪にあわせ
				pwmR = pwmL + P_GAIN_S*currentDiffS
						+ I_GAIN_S*(currentDiffS-preDiffS)*DELTA_T
						- D_GAIN_S*((currentDiffS + preDiffS) / 2.0F)*DELTA_T;

				//上限設定
				if(pwmR > pwmL+MAX_PWM_MARGIN){
					pwmR = pwmL+MAX_PWM_MARGIN;
				}
				else if(pwmR < pwmL-MAX_PWM_MARGIN){
					pwmR = pwmL-MAX_PWM_MARGIN;
				}

				//更新
				preDiff = currentDiff;
				preDiffS = currentDiffS;

				if(timerEnd){
					wheelCtrl.controlWheelsDirect(0, 0);
				}
				else{
					wheelCtrl.controlWheelsDirect((int)pwmL, (int)pwmR);
				}
			}
		};

		timer.scheduleAtFixedRate(task, 0, 4);

		int angleL;	//左回転角度
		int angleR;	//右回転角度
		float angle;	//平均回転角度
		float distance;	//走行距離
		float preDistance = 0.0F;
		int n =0;
		do{
			angleL = motorAngleMeasure.getMotorAngle()[0];
			angleR = motorAngleMeasure.getMotorAngle()[1];
			angle = (angleL+angleR)/2.0F;
			distance = (angle/360)*TIRE_CIRCUMFERENCE;
			LCD.drawString("dist:"+distance+",target:"+targetDistance, 0, 5);
			if(n == 100){
				if(distance == preDistance){
					break;
				}
				preDistance = distance;
				n = 0;
			}
			n++;
		}
		while(distance <= targetDistance);

		timerEnd = true;
		timer.cancel();
	}

	//第一引数で動かす角度、第二引数でブロックを持っているかどうか（持っているときはtrue）
	public void turn(float targetAngle , boolean holdblock)
	{
		targetSpeed = 64; //旋回時は固定
		targetTurnAngle = targetAngle;
		motorAngleMeasure.resetMotorAngle();
		timerEnd = false;
		
		Timer timer = new Timer();
		TimerTask task = new TimerTask(){

			//この辺は可読性の為
			int angleL;	//左回転角度
			int angleR;	//右回転角度
			float turnAngle; //旋回した角度
			float currentDiff; //今回の角度差分
			float preDiff = 0; //以前の角度差分
			float currentDiffS; //今回の速度差分
			float preDiffS = 0; //以前の速度差分
			float pwmL;	//左輪pwm
			float pwmR;	//右輪pwm

			public void run(){

				//距離と速度両方に対してPID制御する
				angleL = motorAngleMeasure.getMotorAngle()[0];
				angleR = motorAngleMeasure.getMotorAngle()[1];
				if(targetTurnAngle > 0.0F){//右旋回
					turnAngle = angleL*220.0F/360.0F;	//ホイール360度で170度機体が旋回するらしい
					currentDiff = (float)targetTurnAngle - turnAngle;	//指定旋回角度を目標
					currentDiffS = (-angleL/1.0F) - (angleR);	//主制御する駆動輪の-1/2が目標だから

					//主制御輪は旋回角度ベース
					pwmL = P_GAIN_A*currentDiff
							+ I_GAIN_A*(currentDiff-preDiff)*DELTA_T
							- D_GAIN_A*((currentDiff + preDiff) / 2.0F)*DELTA_T;

					//上限設定
					if(pwmL > targetSpeed){
						pwmL = targetSpeed;
					}

					//従制御輪は速度ベース≒主制御輪をもとに調整
					pwmR = -(pwmL/1.0F)+P_GAIN_S*currentDiffS
							+ I_GAIN_S*(currentDiffS-preDiffS)*DELTA_T
							- D_GAIN_S*((currentDiffS + preDiffS) / 2.0F)*DELTA_T;

					if(pwmR < -(pwmL/1.0F+MAX_PWM_MARGIN)){
						pwmR = -(pwmL/1.0F+MAX_PWM_MARGIN);
					}

				}
				else {//左旋回
					turnAngle = angleR*220.0F/360.0F;
					currentDiff = -(float)targetTurnAngle - turnAngle;
					currentDiffS = (-angleR/1.0F) - (angleL);

					LCD.drawString(""+currentDiff, 0, 1);
					//主制御輪は旋回角度ベース
					pwmR = P_GAIN_A*currentDiff
							+ I_GAIN_A*(currentDiff-preDiff)*DELTA_T
							- D_GAIN_A*((currentDiff + preDiff) / 2.0F)*DELTA_T;

					LCD.drawString("P:"+(P_GAIN_A*currentDiff)+" "+angleL+" "+angleR, 0, 2);
					LCD.drawString("I:"+(I_GAIN_A*(currentDiff-preDiff)*DELTA_T), 0, 3);
					LCD.drawString("D:"+(D_GAIN_A*((currentDiff + preDiff) / 2.0F)*DELTA_T), 0, 4);

					
					//上限設定
					if(pwmR > targetSpeed){
						pwmR = targetSpeed;
					}
					
					//従制御輪は速度ベース≒主制御輪をもとに調整
					pwmL = (-pwmR/1.0F)+P_GAIN_S*currentDiffS
							+ I_GAIN_S*(currentDiffS-preDiffS)*DELTA_T
							- D_GAIN_S*((currentDiffS + preDiffS) / 2.0F)*DELTA_T;

					//上限設定
					if(pwmL < -(pwmR/1.0F+MAX_PWM_MARGIN)){
						pwmL = -(pwmR/1.0F+MAX_PWM_MARGIN);
					}
				}

				//更新
				preDiff = currentDiff;
				preDiffS = currentDiffS;

				if(timerEnd){
					wheelCtrl.controlWheelsDirect(0, 0);
				}
				else{
					wheelCtrl.controlWheelsDirect((int)pwmL, (int)pwmR);
				}
			}

		};

		timer.scheduleAtFixedRate(task, 0, 4);

		float angleL;	//左回転角度
		float angleR;	//右回転角度
		float turnAngle; //旋回した角度
		float preAngle = 0;
		int n = 0;
		do{
			angleL = motorAngleMeasure.getMotorAngle()[0];
			angleR = motorAngleMeasure.getMotorAngle()[1];
			if(targetTurnAngle > 0.0F){//右旋回
				turnAngle = angleL*220.0F/360.0F;	//ホイール360度で170度機体が旋回するらしい
			}
			else{
				turnAngle = angleR*220.0F/360.0F;
			}
			LCD.drawString("turn:"+turnAngle, 0, 5);

			if(n == 100){
				if(turnAngle == preAngle){
					break;
				}
				preAngle = turnAngle;
				//n = 0;
			}
			n++;

		}
		while(turnAngle <= Math.abs(targetTurnAngle));

		timerEnd = true;
		timer.cancel();
	}

}
