package virtualDevices;

import lejos.utility.Delay;
import Hardware.Hardware;

public class HSVColorDetector {

	//定数は要調整
	//private final float BLACK_BRIGHTNESS = 0.05F;
	private final float RED_HUE = 0.08F;
	private final float GREEN_HUE = 0.45F;
	private final float BLUE_HUE = 0.65F;
	private final float YELLOW_HUE = 0.2F;

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
}
