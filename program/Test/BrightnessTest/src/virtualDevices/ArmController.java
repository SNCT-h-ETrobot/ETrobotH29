package virtualDevices;

import java.util.Timer;
import java.util.TimerTask;

import lejos.utility.Delay;
import Hardware.Hardware;

public class ArmController {

	//カラーセンサーで測定する角度。通常状態
	private final int NORMAL_ANGLE = 25;

	private final float P_GAIN = 2.5F;		//比例定数
	private final float MAX_ABS_PWM = 60.0F;//pwmの絶対値の最大

	public ArmController(){
		resetArm();
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
		Timer timer = new Timer();
		TimerTask task = new TimerTask(){

			public void run(){
				controlArm(NORMAL_ANGLE);
			}
		};
		timer.scheduleAtFixedRate(task, 0, 4);
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
