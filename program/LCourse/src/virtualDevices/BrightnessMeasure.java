package virtualDevices;

import Hardware.Hardware;

public class BrightnessMeasure {
	private float brightness;

	//輝度値基準値。本番環境で調整必要
	private final float BLACK_BRIGHTNESS = 0.05F;
	private final float WHITE_BRIGHTNESS = 0.30F;

	//正規化後の最大・最小値
	private final float BRIGHTNESS_MAX = 1.0F;
	private final float BRIGHTNESS_MIN = 0.0F;

	public float getNormalizedBrightness(){
		float normalizedBrightness = (getBrightness()-BLACK_BRIGHTNESS)/(WHITE_BRIGHTNESS-BLACK_BRIGHTNESS);

		return Math.min(Math.max(normalizedBrightness, BRIGHTNESS_MIN),BRIGHTNESS_MAX);
	}

	public float getBrightness(){
		float[] sampleBright = new float[Hardware.redMode.sampleSize()];
		float a = 0.8F;//大きくすると遅れるが滑らかになる

		Hardware.redMode.fetchSample(sampleBright, 0);
		//出力値 = a*ひとつ前の出力値 + (1-a)*センサ値
		//ひとつ前の出力値はbrightnessとなっている筈
		brightness = a*brightness + (1-a)*sampleBright[0];
		return brightness;
	}

	//比較用に残してあるだけです。masterに入れるとき消して
	public float getBrightnessDirect(){
		float[] sampleBright = new float[Hardware.redMode.sampleSize()];

		//ローパス処理は後回し
		Hardware.redMode.fetchSample(sampleBright, 0);
		//brightness = sampleBright[0];
		//return brightness;
		return sampleBright[0];
	}
}