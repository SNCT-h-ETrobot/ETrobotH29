import lejos.hardware.lcd.LCD;
import virtualDevices.Communicator;
import Information.BlockArrangeInfo;


public class CummuTest {

	public static void main(String[] args) {
		Communicator com = new Communicator();
		BlockArrangeInfo.makeConnection();

		com.establishConnection();
		LCD.drawString("Connect Success", 0, 0);
		int code = com.readCode();
		LCD.drawInt(code, 0, 1);
		BlockArrangeInfo.setBlockPlace(code);
		int[] blockPlace = BlockArrangeInfo.getBlockPlaceIDList();
		LCD.drawInt(blockPlace[0], 0, 2);
		LCD.drawInt(blockPlace[1], 4, 2);
		LCD.drawInt(blockPlace[2], 9, 2);
		LCD.drawInt(blockPlace[3], 0, 3);
		LCD.drawInt(blockPlace[4], 4, 3);
		while(true){}
	}

}
