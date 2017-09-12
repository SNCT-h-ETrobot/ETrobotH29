import virtualDevices.ArmController;
import virtualDevices.BrightnessMeasure;


public class GarageTest {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		GarageIn garageIn = new GarageIn();
		ArmController arm = new ArmController();
		BrightnessMeasure bri = new BrightnessMeasure();

		arm.resetArm();
		arm.controlArmNormalAngel();
		for(int i = 0;i<1500;i++){
			bri.getNormalizedBrightness();
		}
		
		garageIn.run();

	}

}
