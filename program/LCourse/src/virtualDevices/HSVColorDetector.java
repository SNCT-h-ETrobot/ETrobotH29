package virtualDevices;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import Hardware.Hardware;

public class HSVColorDetector {
	private float brightness;

	//輝度値基準値。本番環境で調整必要
	private final float BLACK_BRIGHTNESS = 0.01F;
	private final float WHITE_BRIGHTNESS = 0.52F;

	//正規化後の最大・最小値
	private final float BRIGHTNESS_MAX = 1.0F;
	private final float BRIGHTNESS_MIN = 0.0F;

	//定数は要調整
	//private final float BLACK_BRIGHTNESS = 0.05F;
	private final float RED_HUE = 0.08F;
	private final float GREEN_HUE = 0.45F;
	private final float BLUE_HUE = 0.65F;
	private final float YELLOW_HUE = 0.2F;
	
	private final float UNDER_RED_HUE = 0.02F;
	private final float UNDER_YELLOW_HUE = 0.125F;
	private final float UNDER_BLACK_WHITE_HUE = 0.28F;
	private final float UNDER_GREEN_HUE = 0.35F;
	private final float UNDER_BLUE_HUE = 0.6F;

	private ArmController arm;

	public HSVColorDetector(){
		arm = new ArmController();
		//輝度値検出の赤色光と交互になってしまう場合はHardwareを書き換える
	}

	public float[] detectHSVColor(){
		arm.controlArmDetectAngel();
		Delay.msDelay(800);//アームが動くのを待つ

		float[] sampleRGB = new float[Hardware.RGBMode.sampleSize()];

		Hardware.RGBMode.fetchSample(sampleRGB, 0);

		float r = sampleRGB[0];
		float g = sampleRGB[1];
		float b = sampleRGB[2];

		float max = Math.max(r, Math.max(g, b));
		float min = Math.min(r, Math.min(g, b));

		float h = max - min;
		if(h > 0.0F){
			if(max == r){
				h = (g-b)/h;
				if(h < 0.0F){
					h += 6.0F;
				}
			}
			else if(max == g){
				h = 2.0F + (b-r)/h;
			}
			else{
				h = 4.0F + (r-g)/h;
			}
		}
		h /= 6.0F;

		float s = (max -min);
		if(max != 0.0F){
			s /= max;
		}

		float v = max;

		float[] hsv = new float[3];
		hsv[0] = h;
		hsv[1] = s;
		hsv[2] = v;

		//アームは戻す
		arm.controlArmNormalAngel();
		Delay.msDelay(800);

		return hsv;
	}

	public int getColorID(){
		float[] hsv = detectHSVColor();

		if(hsv[0] < RED_HUE){
			return 1;//赤
		}
		else if(hsv[0] < YELLOW_HUE){
			return 2; //黄
		}
		else if(hsv[0] < GREEN_HUE){
			return 3; //緑
		}
		else if(hsv[0] < BLUE_HUE){
			return 4; //青
		}
		else{
			return 0; //黒
		}
	}
	public int getUnderColorID(){
		

		float[] sampleRGB = new float[Hardware.RGBMode.sampleSize()];

		Hardware.RGBMode.fetchSample(sampleRGB, 0);

		float r = sampleRGB[0];
		float g = sampleRGB[1];
		float b = sampleRGB[2];

		float max = Math.max(r, Math.max(g, b));
		float min = Math.min(r, Math.min(g, b));

		float h = max - min;
		if(h > 0.0F){
			if(max == r){
				h = (g-b)/h;
				if(h < 0.0F){
					h += 6.0F;
				}
			}
			else if(max == g){
				h = 2.0F + (b-r)/h;
			}
			else{
				h = 4.0F + (r-g)/h;
			}
		}
		h /= 6.0F;

		float s = (max -min);
		if(max != 0.0F){
			s /= max;
		}

		float v = max;

		float[] hsv = new float[3];
		hsv[0] = h;
		hsv[1] = s;
		hsv[2] = v;
		//LCD.drawString(""+hsv[1], 1, 1);
		if(hsv[1] > 0.31F)
		{
			if(hsv[0] < UNDER_RED_HUE){
				return 1;//赤
			}
			else if(hsv[0] < UNDER_YELLOW_HUE){
				return 4; //黄
			}
			else if(hsv[0] < UNDER_BLACK_WHITE_HUE){
				return 0; //黒
			}
			else if(hsv[0] < UNDER_GREEN_HUE){
				return 2; //緑
			}
			else if(hsv[0] < UNDER_BLUE_HUE){
				return 3; //青
			}
			else{
				return -1;
			}
		}
		else
		{
			return 0;
		}
	}
	
	public float getNormalizedBrightness(){
		float normalizedBrightness = (getBrightness()-BLACK_BRIGHTNESS)/(WHITE_BRIGHTNESS-BLACK_BRIGHTNESS);

		return Math.min(Math.max(normalizedBrightness, BRIGHTNESS_MIN),BRIGHTNESS_MAX);
	}

	public float getBrightness(){
		float[] sampleBright = new float[Hardware.RGBMode.sampleSize()];
		float a = 0.8F;//大きくすると遅れるが滑らかになる
		
		Hardware.RGBMode.fetchSample(sampleBright, 0);
		float cBrightness = ((sampleBright[0] * 0.299F + sampleBright[1] * 0.587F + sampleBright[2] * 0.114F) * 4.0F); 
		//出力値 = a*ひとつ前の出力値 + (1-a)*センサ値
		//ひとつ前の出力値はbrightnessとなっている筈
		//brightness = a*brightness + (1-a)*sampleBright[0];
		brightness = a*brightness + (1-a)*cBrightness;
		return brightness;
	}

}
