package virtualDevices;

import java.util.Timer;
import java.util.TimerTask;

import lejos.utility.Delay;
import Hardware.Hardware;

public class ArmController {

	//カラーセンサーで測定する角度。通常状態
	private final int NORMAL_ANGLE = 25;
	private final int DETECT_ANGLE = 95;
	private final int HOLD_PRIZE_ANGLE = 110;
	private final int PUT_PRIZE_ANGLE = 0;

	private final float P_GAIN = 2.5F;		//比例定数
	private final float MAX_ABS_PWM = 40.0F;//pwmの絶対値の最大

	private static Timer timer;
	private static Boolean isTask; //タスクがあるか

	public ArmController(){
		if(timer == null){
			timer = new Timer();
			isTask = false;
			resetArm();
		}
	}

	public void controlArm(int angle){
		int tachoCount = Hardware.motorPortA.getTachoCount();
		float pwm = (float)(angle - tachoCount) * P_GAIN;
		if(pwm > MAX_ABS_PWM ){
			pwm = MAX_ABS_PWM;
		}
		else if(pwm < -MAX_ABS_PWM){
			pwm = -MAX_ABS_PWM;
		}

		Hardware.motorPortA.controlMotor((int)pwm, 1);
	}

	public void controlArmNormalAngel(){
		if(isTask){
			timer.cancel();
			timer = new Timer();
		}
		Delay.msDelay(4);
		TimerTask task = new TimerTask(){

			public void run(){
				controlArm(NORMAL_ANGLE);
			}
		};
		timer.scheduleAtFixedRate(task, 0, 4);
		isTask = true;
	}

	public void controlArmDetectAngel(){
		if(isTask){
			timer.cancel();
			timer = new Timer();
		}
		Delay.msDelay(4);
		TimerTask task = new TimerTask(){

			public void run(){
				controlArm(DETECT_ANGLE);
			}
		};
		timer.scheduleAtFixedRate(task, 0, 4);
		isTask = true;
	}
	
	public void controlArmHoldAngel(){
		if(isTask){
			timer.cancel();
			timer = new Timer();
		}
		Delay.msDelay(4);
		TimerTask task = new TimerTask(){

			public void run(){
				controlArm(HOLD_PRIZE_ANGLE);
			}
		};
		timer.scheduleAtFixedRate(task, 0, 4);
		isTask = true;
	}
	
	public void controlArmPutAngel(){
		if(isTask){
			timer.cancel();
			timer = new Timer();
		}
		Delay.msDelay(4);
		TimerTask task = new TimerTask(){

			public void run(){
				controlArm(PUT_PRIZE_ANGLE);
			}
		};
		timer.scheduleAtFixedRate(task, 0, 4);
		isTask = true;
	}

	public void resetArm(){
		int preAngle = -9999;
		Hardware.motorPortA.resetTachoCount();
		while(true){
			Hardware.motorPortA.controlMotor(-20, 1);
			Delay.msDelay(100);
			if(preAngle == Hardware.motorPortA.getTachoCount()){
				Hardware.motorPortA.resetTachoCount();
				break;
			}
			preAngle = Hardware.motorPortA.getTachoCount();
		}
	}

}
