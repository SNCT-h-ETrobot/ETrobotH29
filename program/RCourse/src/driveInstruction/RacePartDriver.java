package driveInstruction;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import virtualDevices.ArmController;
import virtualDevices.DistanceMeasure;
import Information.SectionInfo;
import driveControl.Linetracer;

public class RacePartDriver {

	private DistanceMeasure disMeasure = new DistanceMeasure();
	private ArmController armCtrl;
	private Linetracer tracer;

	private ArrayList<SectionInfo> sectionList;
	private int currentSectionID;

	public RacePartDriver(int courseID){
		//区間情報はリストで管理する
		sectionList = new ArrayList<SectionInfo>();

		//コースIDによってLとRどちらを走行するか決定する
		if(courseID == 1){	//Lコース
			sectionList.add(new SectionInfo(0,1000.0F,-100.0F,10.0F,10.0F,0.15F,80.0F));
			sectionList.add(new SectionInfo(1,1001.0F,50.0F,0.0F,0.0F,0.2F,0.0F));
			sectionList.add(new SectionInfo(2,1002.0F,50.0F,0.0F,0.0F,0.2F,0.0F));
		}
		else if(courseID == 2){	//Rコース
			//最初のカーブまで260
			sectionList.add(new SectionInfo(0,260.0F,60.0F,5.0F,4.0F,0.5F,100.0F));
			//GATE1まで492
			sectionList.add(new SectionInfo(1,492.0F,85.0F,10.0F,5.0F,0.5F,100.0F));
			//GATE2前のヘアピンカーブにはいるまで649
			sectionList.add(new SectionInfo(2,649.0F,80.0F,10.0F,5.0F,0.5F,100.0F));
			//GATE2前の180°カーブを抜けてカーブを抜けるまで790
			sectionList.add(new SectionInfo(3,755.0F,85.0F,30.0F,5.0F,0.5F,100.0F));
			//ゴールまで1090だが、灰色検知のため余裕をもつ
			sectionList.add(new SectionInfo(4,1045.0F,60.0F,5.0F,4.0F,0.5F,100.0F));
		}

		armCtrl = new ArmController();
		tracer = new Linetracer();

	}

	public void driveRacePart(){

		Timer timer = new Timer();
		
		TimerTask timerTask = new TimerTask(){
			
			public void run(){
				presumeCurrentSection();
				if(currentSectionID != -1){
					tracer.linetraceFast(sectionList.get(currentSectionID).getKp(),
							sectionList.get(currentSectionID).getKi(),
							sectionList.get(currentSectionID).getKd(),
							sectionList.get(currentSectionID).getTargetBrightness(),
							sectionList.get(currentSectionID).getTargetForward());
				}
				
			}
		};

		timer.scheduleAtFixedRate(timerTask, 0, 4);

			armCtrl.controlArmNormalAngel();
		
		while(currentSectionID != -1)
		{
			
		}
		timer.cancel();
		
	}
	

	private void presumeCurrentSection(){
		float distance = disMeasure.getDistance();

		for(int i = 0; i < sectionList.size();i++){
			//区間の距離と走行した距離を比較
			if(sectionList.get(i).getDistance() > distance){
				currentSectionID = sectionList.get(i).getSectionID();
				break;
			}
			if(i + 1 == sectionList.size()){
				//もし区間情報リストの終端まで比較しても
				//走行済の距離のほうが大きい場合例外として-1
				currentSectionID = -1;
			}
		}
		
	}
}
