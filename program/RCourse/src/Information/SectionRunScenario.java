package Information;

import sectionRun.DetectGray;
import sectionRun.DetectTrain;
import sectionRun.GarageIn;
import sectionRun.HoldPrize;
import sectionRun.MoveArea;
import sectionRun.MoveBlock;
import sectionRun.PassRail;
import sectionRun.PutPrize;
import sectionRun.SectionRun;

public class SectionRunScenario {

	private final int RED_COLOR_ID =1;
	private final int YELLOW_COLOR_ID =2;
	private final int BLUE_COLOR_ID =3;
	private final int GREEN_COLOR_ID =4;
	private SectionRun[] scenario = new SectionRun[29];
	
	public SectionRunScenario()
	{
		scenario[0] = new DetectGray();
		scenario[1] = new DetectTrain(0); //一回目検知、引数これであってる?
		scenario[2] = new PassRail();
		//エリアIDは適当
		scenario[3] = new MoveArea(1); //右手前まで移動
		scenario[4] = new MoveBlock(YELLOW_COLOR_ID,false);
		scenario[5] = new MoveArea(2); //左手前まで移動
		scenario[6] = new MoveBlock(GREEN_COLOR_ID,true);
		scenario[7] = new MoveArea(3); //左奥まで移動
		scenario[8] = new MoveBlock(BLUE_COLOR_ID,false);
		scenario[9] = new MoveArea(4); //右奥まで移動
		scenario[10] = new MoveBlock(RED_COLOR_ID,true);
		scenario[11] = new MoveArea(5); //エリア出口へ移動
		scenario[12] = new DetectTrain(1); //二回目検知
		scenario[13] = new PassRail();

		scenario[14] = new MoveArea(1); //右手前まで移動
		scenario[15] = new MoveBlock(BLUE_COLOR_ID,false);
		scenario[16] = new MoveArea(2); //左手前まで移動
		scenario[17] = new MoveBlock(RED_COLOR_ID,true);
		scenario[18] = new MoveArea(3); //左奥まで移動
		scenario[19] = new MoveBlock(YELLOW_COLOR_ID,false);
		scenario[20] = new MoveArea(4); //右奥まで移動
		scenario[21] = new MoveBlock(GREEN_COLOR_ID,true);
		scenario[22] = new MoveArea(6); //懸賞へ移動
		scenario[23] = new HoldPrize();
		scenario[24] = new MoveArea(7); //懸賞からエリア出口へ移動
		scenario[25] = new DetectTrain(2); //三回目検知、ここは時間検知？
		scenario[26] = new PassRail();

		scenario[27] = new PutPrize();
		scenario[28] = new GarageIn();

	}

	public SectionRun[] getScenario(){
		return scenario;
	}
}
