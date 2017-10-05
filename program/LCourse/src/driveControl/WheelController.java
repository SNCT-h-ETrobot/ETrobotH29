package driveControl;

import Hardware.Hardware;

public class WheelController {

	public void controlWheels(float turn,float forward){
		//計算式は適当
		//turnのMAXを50(-50)の想定のとき
		//外輪と内輪のパワー差が２倍になる感じ
		int powerL;
		int powerR;
		if(turn >= 0){
			powerL = (int)((100-turn)/100 * forward);
		}
		else{
			powerL = (int)forward;
		}
		if(turn <= 0){
			powerR = (int)((100+turn)/100 * forward);
		}
		else{
			powerR = (int)forward;
		}

		controlWheelsDirect(powerL,powerR);
	}
	
	public void controlWheelsFast(float turn,float forward){
		//レース部のみ
		int powerL;
		int powerR;
		if(turn >= 0){
			powerL = (int)((100-turn*1.3)/100 * forward);
		}
		else{
			powerL = (int)forward;
		}
		if(turn <= 0){
			powerR = (int)((100+turn*1.3)/100 * forward);
		}
		else{
			powerR = (int)forward;
		}

		controlWheelsDirect(powerL,powerR);
	}

	public void controlWheelsDirect(int powerL,int powerR){

		Hardware.motorPortL.controlMotor(powerL, 1);
		Hardware.motorPortR.controlMotor(powerR, 1);
	}

}
