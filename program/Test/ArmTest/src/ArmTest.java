import Hardware.Hardware;
import virtualDevices.ArmController;


public class ArmTest {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		ArmController arm = new ArmController();
		float[] sampleTouch = new float[Hardware.touch.sampleSize()];
		while(true){
			Hardware.touchMode.fetchSample(sampleTouch, 0);
			if(sampleTouch[0] != 0){
				arm.controlArmNormalAngel();
			}else{
				arm.controlArm(0);
			}

		}
	}

}
