import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import virtualDevices.ArmController;
import virtualDevices.BrightnessMeasure;
import virtualDevices.DistanceMeasure;
import virtualDevices.MotorAngleMeasure;
import Hardware.Hardware;
import driveControl.DistanceAngleController;


public class MotorControlTest {
	private static ArmController arm = new ArmController();
	private static BrightnessMeasure bri = new BrightnessMeasure();
	private static DistanceMeasure disM = new DistanceMeasure();
	private static MotorAngleMeasure mAM = new MotorAngleMeasure();
	private static DistanceAngleController distCtrl = new DistanceAngleController();

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		arm.controlArmNormalAngle();
		for(int i = 0; i<1500;i++){
			bri.getBrightness();
			disM.getDistance();
			mAM.getMotorAngle();
			Hardware.motorPortL.controlMotor(0, 1);
			Hardware.motorPortR.controlMotor(0, 1);
		}
		LCD.drawString(""+mAM.getMotorAngle()[0], 0, 0);

		//distCtrl.goStraightAhead(100.0F, 50.0F);
		distCtrl.turn(-90.0F, false);
		Delay.msDelay(4);
		Hardware.motorPortL.controlMotor(0, 1);
		Hardware.motorPortR.controlMotor(0, 1);
		LCD.drawString("END", 0, 3);	

	}

}
