package driveControl;

import java.util.Timer;
import java.util.TimerTask;

import lejos.utility.Delay;
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
	private final float P_GAIN_S = 3.0F;//速度用P係数
	private final float I_GAIN_S = 1.0F;//速度用I係数
	private final float D_GAIN_S = 0.1F;//速度用D係数

	private final float P_GAIN_A = 3.5F;//旋回角度用P係数
	private final float I_GAIN_A = 2.0F;//旋回角度用I係数
	private final float D_GAIN_A = 0.5F;//旋回角度用D係数
	private final float P_GAIN_AS = 3.5F;//速度用P係数
	private final float I_GAIN_AS = 2.0F;//速度用I係数
	private final float D_GAIN_AS = 0.5F;//速度用D係数

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
		int n = 0;
		do{
			angleL = motorAngleMeasure.getMotorAngle()[0];
			angleR = motorAngleMeasure.getMotorAngle()[1];
			angle = (angleL+angleR)/2.0F;
			distance = (angle/360)*TIRE_CIRCUMFERENCE;
			//LCD.drawString("dist:"+distance+",target:"+targetDistance, 0, 5);
			if(distance == preDistance){
				if(n == 50){
					break;
				}
			}else{
				preDistance = distance;
				n = 0;
			}
			n++;
			Delay.msDelay(4);
		}
		while(Math.abs(distance) <= Math.abs(targetDistance));

		timerEnd = true;
		Delay.msDelay(4);
		timer.cancel();
		wheelCtrl.controlWheelsDirect(0, 0);
	}

	//第一引数で動かす角度、第二引数でブロックを持っているかどうか（持っているときはtrue）
	public void turn(float targetAngle , boolean holdblock)
	{

		targetSpeed = 55; //旋回時は固定
		targetTurnAngle = targetAngle;
		motorAngleMeasure.resetMotorAngle();
		timerEnd = false;

		Timer timer = new Timer();
		TimerTask task = new TimerTask(){

			//この辺は可読性の為
			int angleL;	//左回転角度
			int angleR;	//右回転角度
			float turnAngleL; //旋回した左輪ベースの角度
			float turnAngleR; //旋回した右輪ベースの角度
			float currentDiffL; //今回の左輪角度差分
			float preDiffL = 0; //以前の左輪角度差分
			float currentDiffR; //今回の右輪角度差分
			float preDiffR = 0; //以前の左輪角度差分
			float pwmL;	//左輪pwm
			float pwmR;	//右輪pwm


			public void run(){

				//両輪に対して角度でPID制御する
				angleL = motorAngleMeasure.getMotorAngle()[0];
				angleR = motorAngleMeasure.getMotorAngle()[1];

				if(targetTurnAngle > 0)//個体差による微調整
				{
					turnAngleL = angleL*222.0F/360.0F;	//ホイール140度くらいで90度機体が旋回するらしい

					turnAngleR = angleR*222.0F/360.0F;

					currentDiffL = (float)targetTurnAngle - turnAngleL;	//指定旋回角度を目標
					currentDiffR = (-1.0F)*(float)targetTurnAngle - turnAngleR;	//指定旋回角度の逆を目標(Rは応答が悪い?)
					//旋回角度ベースで制御
					pwmL = P_GAIN_A*currentDiffL
							+ I_GAIN_A*(currentDiffL-preDiffL)*DELTA_T
							- D_GAIN_A*((currentDiffL + preDiffL) / 2.0F)*DELTA_T + 20;

					//上限設定
					if(pwmL > targetSpeed + 20){
						pwmL = targetSpeed + 20;
					}

					if(pwmL < -targetSpeed){
						pwmL = -targetSpeed;
					}

					//右輪も旋回角度ベースで制御
					pwmR = P_GAIN_AS*currentDiffR
							+ I_GAIN_AS*(currentDiffR-preDiffR)*DELTA_T
							- D_GAIN_AS*((currentDiffR + preDiffR) / 2.0F)*DELTA_T;

					//上限設定
					if(pwmR > targetSpeed){
						pwmR = targetSpeed;
					}

					if(pwmR < -targetSpeed){
						pwmR = -targetSpeed;
					}

				}
				else
				{
					turnAngleL = angleL*216.0F/360.0F;

					turnAngleR = angleR*216.0F/360.0F;

					currentDiffL = (float)targetTurnAngle - turnAngleL;	//指定旋回角度を目標
					currentDiffR = (-1.0F)*(float)targetTurnAngle - turnAngleR;	//指定旋回角度の逆を目標(Rは応答が悪い?)

					//旋回角度ベースで制御
					pwmL = P_GAIN_A*currentDiffL
							+ I_GAIN_A*(currentDiffL-preDiffL)*DELTA_T
							- D_GAIN_A*((currentDiffL + preDiffL) / 2.0F)*DELTA_T;

					//上限設定
					if(pwmL > targetSpeed){
						pwmL = targetSpeed;
					}

					if(pwmL < -targetSpeed){
						pwmL = -targetSpeed;
					}

					//右輪も旋回角度ベースで制御
					pwmR = P_GAIN_AS*currentDiffR
							+ I_GAIN_AS*(currentDiffR-preDiffR)*DELTA_T
							- D_GAIN_AS*((currentDiffR + preDiffR) / 2.0F)*DELTA_T + 10;

					//上限設定
					if(pwmR > targetSpeed + 10){
						pwmR = targetSpeed + 10;
					}

					if(pwmR < -targetSpeed){
						pwmR = -targetSpeed;
					}

				}


				//LCD.drawString("PL:"+(P_GAIN_A*currentDiffL)+" "+angleL+" "+angleR, 0, 2);
				//LCD.drawString("PR:"+(P_GAIN_AS*currentDiffR)+" "+angleL+" "+angleR, 0, 3);

				//更新
				preDiffL = currentDiffL;
				preDiffR = currentDiffR;
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
		//float angleR;	//右回転角度
		float turnAngle; //旋回した角度
		float preAngle = 0;
		int n = 0;
		do{
			angleL = motorAngleMeasure.getMotorAngle()[1];
			//angleR = motorAngleMeasure.getMotorAngle()[1];

			turnAngle = angleL*222.0F/360.0F;	//ホイール140度くらいで90度機体が旋回するらしい
			//LCD.drawString("turnL:"+turnAngle, 0, 5);
			//LCD.drawString("turnR:"+angleR*230.0F/360.0F, 0, 6);
			if(turnAngle == preAngle){
				if(n == 50){
					break;
				}
			}else{
				preAngle = turnAngle;
				n = 0;
			}
			n++;
			Delay.msDelay(4);
		}
		while(Math.abs(turnAngle) <= Math.abs(targetTurnAngle));

		timerEnd = true;
		Delay.msDelay(4);
		timer.cancel();
		wheelCtrl.controlWheelsDirect(0, 0);
	}

	public void TurningControl(float targetDistance , float speed , final boolean right)
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

			public void run(){
				angleL = motorAngleMeasure.getMotorAngle()[0] * 2;
				angleR = motorAngleMeasure.getMotorAngle()[1] * 2;
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
					if(right)
					{
						wheelCtrl.controlWheelsDirect(0, (int)pwmR);
					}
					else
					{
						wheelCtrl.controlWheelsDirect((int)pwmL, 0);
					}
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
			//LCD.drawString("dist:"+distance+",target:"+targetDistance, 0, 5);
			if(n == 5000){
				if(distance == preDistance){
					break;
				}
				preDistance = distance;
				n = 0;
			}
			n++;
		}
		while(Math.abs(distance) <= Math.abs(targetDistance));

		timerEnd = true;
		timer.cancel();
	}

}
