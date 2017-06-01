package driveInstruction;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import virtualDevices.ArmController;
import virtualDevices.DistanceMeasure;
import Information.SectionInfo;
import driveControl.Linetracer;

public class RacePartDriver {

	private DistanceMeasure disMeasure;
	private ArmController armCtrl;
	private Linetracer tracer;

	private ArrayList<SectionInfo> sectionList;
	private int currentSectionID;

	public RacePartDriver(int courseID){
		//区間情報はリストで管理する
		sectionList = new ArrayList<SectionInfo>();

		//コースIDによってLとRどちらを走行するか決定する
		if(courseID == 1){	//Lコース
			sectionList.add(new SectionInfo(0,8.0F,50.0F,0.0F,0.0F,0.2F,100.0F));
			sectionList.add(new SectionInfo(1,18.0F,50.0F,0.0F,0.0F,0.2F,100.0F));
			sectionList.add(new SectionInfo(2,30.0F,50.0F,0.0F,0.0F,0.2F,0.0F));
		}
		else if(courseID == 2){	//Rコース
			sectionList.add(new SectionInfo(0,10.0F,50.0F,0.0F,0.0F,0.2F,100.0F));
			sectionList.add(new SectionInfo(1,20.0F,50.0F,0.0F,0.0F,0.2F,100.0F));
			sectionList.add(new SectionInfo(2,30.0F,50.0F,0.0F,0.0F,0.2F,0.0F));
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
					tracer.linetrace(sectionList.get(currentSectionID).getKp(),
							sectionList.get(currentSectionID).getKi(),
							sectionList.get(currentSectionID).getKd(),
							sectionList.get(currentSectionID).getTargetBrightness(),
							sectionList.get(currentSectionID).getTargetForward());
				}
			}
		};

		timer.scheduleAtFixedRate(timerTask, 0, 4);

		while(currentSectionID != -1){
			armCtrl.controlArmNormalAngel();
		}
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
