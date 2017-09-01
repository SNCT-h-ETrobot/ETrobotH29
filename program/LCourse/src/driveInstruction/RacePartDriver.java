package driveInstruction;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Hardware.Hardware;
import virtualDevices.ArmController;
import virtualDevices.Communicator;
import virtualDevices.DistanceMeasure;
import Information.SectionInfo;
import driveControl.DetectGray;
import driveControl.Linetracer;
import driveControl.WheelController;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class RacePartDriver {

	private DistanceMeasure disMeasure = new DistanceMeasure();
	private ArmController armCtrl;
	private Linetracer tracer;

	//private Communicator com;
	
	private ArrayList<SectionInfo> sectionList;
	private int currentSectionID;
	private DetectGray detectGray = new DetectGray();
	
	
	
	//private WheelController whcon = new WheelController();
	
	

	public RacePartDriver(int courseID){
		//区間情報はリストで管理する
		sectionList = new ArrayList<SectionInfo>();

		//コースIDによってLとRどちらを走行するか決定する
		if(courseID == 1){	//Lコース
			/*
			sectionList.add(new SectionInfo(0,260.0F,-150.0F,-100.0F,-10.0F,0.5F,100.0F));
			sectionList.add(new SectionInfo(1,380.0F,-60.0F,-60.0F,-5.0F,0.5F,100.0F));
			sectionList.add(new SectionInfo(2,658.0F,-150.0F,-100.0F,-10.0F,0.5F,100.0F));
			sectionList.add(new SectionInfo(3,750.0F,-60.0F,-60.0F,-5.0F,0.5F,100.0F));
			sectionList.add(new SectionInfo(4,1062.0F,-150.0F,-100.0F,-10.0F,0.5F,100.0F)); 
			*/
			
			//最初のカーブまで260cm
			sectionList.add(new SectionInfo(0,260.0F,-50.0F,-10.0F,-5.0F,0.5F,100.0F));
			//GATE1まで380
			sectionList.add(new SectionInfo(1,380.0F,-70.0F,-60.0F,-6.0F,0.5F,100.0F));
			//GATE2まで658
			sectionList.add(new SectionInfo(2,658.0F,-80.0F,-5.0F,-5.0F,0.5F,100.0F));
			//GATE2の急激なカーブに入る所が750
			sectionList.add(new SectionInfo(3,750.0F,-80.0F,-30.0F,-5.0F,0.5F,100.0F));
			//GATE2の急激なカーブを抜ける所が900
			sectionList.add(new SectionInfo(4,900.0F,-130.0F,-80.0F,-5.0F,0.5F,100.0F));
			//ゴールが1067なので、灰色検知のために余裕を持つ
			sectionList.add(new SectionInfo(5,1022.0F,-60.0F,-10.0F,-5.0F,0.5F,100.0F));
			//速度を落として安定走行させる
			sectionList.add(new SectionInfo(6,1032.0F,-100.0F,-20.0F,-5.0F,0.5F,60.0F));
			
			//sectionList.add(new SectionInfo(0,100.0F,-100.0F,-20.0F,-5.0F,0.5F,60.0F));

			
			
		}
		else if(courseID == 2){	//Rコース
			sectionList.add(new SectionInfo(0,260.0F,-150.0F,-100.0F,-10.0F,0.5F,100.0F));
			sectionList.add(new SectionInfo(1,492.0F,-60.0F,-60.0F,-5.0F,0.5F,100.0F));
			sectionList.add(new SectionInfo(2,649.0F,-150.0F,-100.0F,-10.0F,0.5F,100.0F));
			sectionList.add(new SectionInfo(3,740.0F,-60.0F,-60.0F,-5.0F,0.5F,100.0F));
			sectionList.add(new SectionInfo(4,1092.0F,-150.0F,-100.0F,-10.0F,0.5F,100.0F)); 
			//sectionList.add(new SectionInfo(0,1092.0F,-150.0F,-100.0F,-10.0F,0.5F,100.0F));
		}

		armCtrl = new ArmController();
		tracer = new Linetracer();

	}

	public void driveRacePart(){
		//com = new Communicator();
		//LCD.drawString("Connect Ready", 0, 0);
		//com.establishConnection();
		
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

		//com.readCode();
		//LCD.drawString("dist:"+disMeasure.getDistance(), 0, 0);
		armCtrl.controlArmNormalAngel();
		
		while(currentSectionID != -1)
		{
			
		}
		timer.cancel();
		detectGray.run();
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
