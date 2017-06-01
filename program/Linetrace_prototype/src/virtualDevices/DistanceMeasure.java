package virtualDevices;

import Hardware.Hardware;

public class DistanceMeasure {
	private float distance;

	public float getDistance(){

		float rotate = (Hardware.motorPortL.getTachoCount() + Hardware.motorPortR.getTachoCount())/2.0F;

		distance = (rotate / 360.0F) * 26.2F; //センチメートルにする

		return distance;
	}

	public void resetDistance(){
		Hardware.motorPortL.resetTachoCount();
		Hardware.motorPortR.resetTachoCount();
		this.distance = 0.0F;
	}

}
