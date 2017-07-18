package virtualDevices;

import Hardware.Hardware;

public class BrightnessMeasure {
	private float brightness;

	public float getBrightness(){
		float[] sampleBright = new float[Hardware.redMode.sampleSize()];

		//ローパス処理は後回し
		Hardware.redMode.fetchSample(sampleBright, 0);
		brightness = sampleBright[0];
		return brightness;
	}
}
