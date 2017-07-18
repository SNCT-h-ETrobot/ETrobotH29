package virtualDevices;

import Hardware.Hardware;

public class MotorAngleMeasure{
	private int motorAngleL;
	private int motorAngleR;

	public int[] getMotorAngle(){
		int[] motorAngleLR = new int[2];

		motorAngleL = Hardware.motorPortL.getTachoCount();
		motorAngleR = Hardware.motorPortR.getTachoCount();

		motorAngleLR[0] = motorAngleL;
		motorAngleLR[1] = motorAngleR;

		return motorAngleLR;
	}

	public void resetMotorAngle(){
		Hardware.motorPortL.resetTachoCount();
		Hardware.motorPortR.resetTachoCount();
	}

}
