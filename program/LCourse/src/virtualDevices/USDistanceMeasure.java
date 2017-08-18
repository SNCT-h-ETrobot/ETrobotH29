package virtualDevices;

import Hardware.Hardware;

public class USDistanceMeasure {
	private float distance;

	public float getDistance(){
		float[] sample = new float[Hardware.distanceMode.sampleSize()];
		Hardware.distanceMode.fetchSample(sample, 0);

		distance = sample[0];
		return distance;
	}

}
