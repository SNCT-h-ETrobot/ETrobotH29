package driveInstruction;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import virtualDevices.ArmController;
import virtualDevices.DistanceMeasure;
import virtualDevices.HSVColorDetector;
import Information.BlockArrangeInfo;
import Information.DriveInfo;
import Information.Path;
import Information.RouteDriver;
import driveControl.DistanceAngleController;
import driveControl.Linetracer;
import driveControl.WheelController;

public class GamePartDriver {

	private float currentAngle;
	private int currentID;
	private ArrayList<DriveInfo> missionScenario;
	private boolean isLinetrace;

	private ArmController arm;
	private Linetracer linetracer;
	private DistanceMeasure distMeasure;
	private DistanceAngleController dACtrl;
	private RouteDriver routeDriver;

	private final float P_GAIN = 100.0F;	//P係数
	private final float I_GAIN = 10.0F;	//I係数
	private final float D_GAIN = 5.0F;	//D係数

	private final float TARGET_BRIGHTNESS = 0.5F;//要調整もっと高いかも
	
	private float blockMoveCorrection = 0.0F;
	private final float LINETRACE_ANGLE_CONNECTION = 6.0F;
	private final float BLOCK_MOVE_DISTANCE = 5.0F;
	
	private HSVColorDetector colorDetect = new HSVColorDetector();
	private WheelController whcon = new WheelController();

	public GamePartDriver(int courceID)
	{
		dACtrl = new DistanceAngleController();
		missionScenario = new ArrayList<DriveInfo>();
		routeDriver = new RouteDriver();
		linetracer = new Linetracer();
		distMeasure = new DistanceMeasure();
		arm = new ArmController();

		//Lコース
		if(courceID == 1)
		{
			//ブロック並べに突入するときの進行方向は右上方向なので300度
			currentAngle = 300.000F;
			currentID = 90;
		}
		//Rコース
		else if(courceID == 2)
		{

		}

	}

	public void driveGamePart()
	{
		arm.controlArmNormalAngel();
		createMissionScenario(routeDriver.getRoute());

		LCD.drawString("Size:" + missionScenario.size(), 0, 0);
		Delay.msDelay(3000);
		//緑色検知
		int cnt = 0;
		Timer timerGreen = new Timer();
		TimerTask greenTask = new TimerTask(){
			public void run(){
				if(isLinetrace)linetracer.linetrace(P_GAIN, I_GAIN, D_GAIN, TARGET_BRIGHTNESS, 50,colorDetect.getNormalizedBrightness());
			}
		};
		isLinetrace = true;
		timerGreen.scheduleAtFixedRate(greenTask, 0, 4);
		while(true)
		{
			if(colorDetect.getUnderColorID() == 3)
			{
				break;
			}

			//Delay.msDelay(2);
		}
		isLinetrace = false;
		timerGreen.cancel();
		whcon.controlWheelsDirect(0, 0);
		Delay.msDelay(200);
		
		
		float distance = 0;
		float speed = 0;
		//dACtrl.turn(20.0F,false);
		for(int i=0;i<missionScenario.size();i++)
		{

			//LCD.drawString("turn:"+missionScenario.get(i).getTurnAngle(), 0, 1);


			if(missionScenario.get(i).getHoldBlock())
			{
				if(missionScenario.get(i).getTurnAngle() == 180.0F || missionScenario.get(i).getTurnAngle() == -180.0F)
				{
					
					//r=12.6cm,a=65°
					dACtrl.TurningControl((float) (2.0F * Math.PI * 12.6F * 65.0F / 360.0F), 80, false);
					dACtrl.TurningControl((float) (2.0F * Math.PI * 12.6F * ((65.0F*2.0F + 180.0F) / 360.0F)), 80, true);
					if(missionScenario.get(i).getLinetrace()){//次がライントレースの時に右側よりにする 
						dACtrl.TurningControl((float) (2.0F * Math.PI * 12.6F * 65.0F / 360.0F), 80, false);
					}
					else
					{
						dACtrl.TurningControl((float) (2.0F * Math.PI * 12.6F * 65.0F / 360.0F), 80, false);
					}
					blockMoveCorrection += 5.0F;
				}
				else if(missionScenario.get(i).getTurnAngle() == 0.000F)
				{
					if(missionScenario.get(i).getLinetrace()){//次がライントレースの時に右側よりにする 
						dACtrl.turn(LINETRACE_ANGLE_CONNECTION,true);
					}
				}
				else
				{
					if(missionScenario.get(i).getTurnAngle() >= 60.000F || missionScenario.get(i).getTurnAngle() <= -60.000F)
					{
						if(missionScenario.get(i).getLinetrace()){//次がライントレースの時に右側よりにする
							//右回りの時は少し強め 左回りの時は少し弱め
							float theta = missionScenario.get(i).getTurnAngle()/2.0F + (LINETRACE_ANGLE_CONNECTION) / 2.0F;
							dACtrl.turn(theta,missionScenario.get(i).getHoldBlock());
							//少しだけ直進
							dACtrl.goStraightAhead(BLOCK_MOVE_DISTANCE, 60.000F);
							dACtrl.turn(theta,missionScenario.get(i).getHoldBlock());
							
							blockMoveCorrection = (float) ((BLOCK_MOVE_DISTANCE/2.0F) / Math.cos(theta * Math.PI / 180.0F));
						}
						else
						{
							float theta = missionScenario.get(i).getTurnAngle()/2.0F;
							dACtrl.turn(theta,missionScenario.get(i).getHoldBlock());
							//少しだけ直進
							dACtrl.goStraightAhead(BLOCK_MOVE_DISTANCE, 60.000F);
							dACtrl.turn(theta,missionScenario.get(i).getHoldBlock());
							blockMoveCorrection = (float) ((BLOCK_MOVE_DISTANCE/2.0F) / Math.cos(theta * Math.PI / 180.0F));
						}
					}
					else
					{
						dACtrl.turn(missionScenario.get(i).getTurnAngle(),missionScenario.get(i).getHoldBlock());
					}
				
					
				}
			}
			else//ブロックを持ってない時
			{
				if(missionScenario.get(i).getLinetrace())//ライントレース時の補正
				{
						//右回りの時は少し強め 左回りの時は少し弱め
						dACtrl.turn(missionScenario.get(i).getTurnAngle() + LINETRACE_ANGLE_CONNECTION,missionScenario.get(i).getHoldBlock());
				}
				else
				{
					dACtrl.turn(missionScenario.get(i).getTurnAngle(),missionScenario.get(i).getHoldBlock());
				}
			}
			
			if(i<missionScenario.size() - 1)
			{
			if(missionScenario.get(i+1).getTurnAngle() == 0.0F && missionScenario.get(i+1).getDistance() > 0.0F && missionScenario.get(i+1).getDistance() > 0.0F){
				//直線の時はいちいち止まらずつなげる
				distance = missionScenario.get(i).getDistance()+missionScenario.get(i+1).getDistance();
				speed = missionScenario.get(i+1).getSpeed();
				i++;
			}
			else{
				distance = missionScenario.get(i).getDistance();
				speed = missionScenario.get(i).getSpeed();
			}
			}
			if(missionScenario.get(i).getLinetrace()){
				if(distance >= 0.0f)
				{
					//dACtrl.goStraightAhead((distance - blockMoveCorrection)/8.0F,speed);
					dACtrl.goStraightAhead((distance - blockMoveCorrection + 1.0F)/8.0f,speed);
					//1/8～6/8の区間の間だけライントレース
					distMeasure.resetDistance();
					//後で直す
					/*
					while(distMeasure.getDistance()<(distance - blockMoveCorrection)/8.0F*6.0F){//ブロックの分微調整
						linetracer.linetrace(P_GAIN, I_GAIN, D_GAIN, TARGET_BRIGHTNESS, speed);
						Delay.msDelay(4);
					}
					*/
					Timer timer = new Timer();
					final float DISTANCE = distance;
					final float SPEED = speed;
					TimerTask timerTask = new TimerTask(){
						
						public void run(){
							if(distMeasure.getDistance()<(DISTANCE - blockMoveCorrection + 1.0F)/8.0f*5.0f)
							{
								linetracer.linetrace(P_GAIN, I_GAIN, D_GAIN, TARGET_BRIGHTNESS, SPEED);
							}
						}
					};

					timer.scheduleAtFixedRate(timerTask, 0, 4);

					while(true)
					{
						if(distMeasure.getDistance()>=(distance - blockMoveCorrection + 1.0F)/8.0f*5.0f)
						{
							linetracer.linetrace(P_GAIN, I_GAIN, D_GAIN, TARGET_BRIGHTNESS, 0);
							timer.cancel();
							break;
						}
					}
					dACtrl.goStraightAhead((distance - blockMoveCorrection + 1.0F)/4.0f,speed);
					blockMoveCorrection = 0.0F;
				}
				else
				{
					dACtrl.goStraightAhead((distance - blockMoveCorrection),speed);
					blockMoveCorrection = 0.0F;
				}
			}

			else{
				//LCD.drawString("distance:"+missionScenario.get(i).getDistance(), 0, 2);
				dACtrl.goStraightAhead(distance - blockMoveCorrection,speed);
				blockMoveCorrection = 0.0F;
			}
		}

	}

	private void createMissionScenario(List<Integer> route)
	{
		//頂点をどのような順番で通るかにより、各値を求めてリストに格納

		float correctedAngle = 0;
		float correctedDistance = 0;
		boolean blockhold = false;
		boolean lineflag = true;
		int[] blockList = BlockArrangeInfo.getBlockPlaceIDList();

		for(int i=1;i<route.size();i+=2)
		{
			if(i == 1)
			{
				for(int j =0;j<blockList.length;j++)
				{
					if(blockList[j] == 90)
					{
						blockhold = true;
						break;
					}
					
				}
				missionScenario.add(new DriveInfo(0.000F,false,8.000F,60,false) );
			}

			if(currentAngle >= 360.0F)
			{
				currentAngle -= 360.0F;
			}
			else if(currentAngle <= -360.0F)
			{
				currentAngle += 360.0F;
			}
			
			//目的地に到着
			if(route.get(i).equals(route.get(i-1)))
			{
				//ブロックを置く処理
				if(blockhold)
				{
					if(route.get(i-2).equals(route.get(i+1)))
					{
						Path path = (Path)BlockArrangeInfo.getPointObject(route.get(i+1));
						missionScenario.add(new DriveInfo(0.000F,false,-path.getDistance()+8.0F,60,false) );
						currentID = route.get(i+2);
						i += 1;
						
						blockhold = !blockhold;
						continue;
					}
					else
					{
						missionScenario.add(new DriveInfo(0.000F,false,-15.000F,60,false) );
						correctedAngle = currentAngle;
						lineflag = false;
						correctedDistance += -15.000F-8.0F;
					}
				}
				i--;
				blockhold = !blockhold;
			}
			else
			{
				Path path = (Path)BlockArrangeInfo.getPointObject(route.get(i));

				float angle = path.getAngle() - currentAngle;
				if(currentID > route.get(i+1))
				{
					angle += 180.000F;
				}

				if( (angle > 180.000F) || (angle < -180.000F) )
				{
					if(angle > 0.000F)
					{
						angle -= 360.000F;
					}
					else
					{
						angle += 360.000F;
					}

				}
				//System.out.println("i:" +i+ " "+ currentID + "->" + route.get(i+1) + " angle:"+angle);
				if(i<route.size()-3)
				{
					if(route.get(i+1).equals(route.get(i+2)) && blockhold)
					{
						missionScenario.add(new DriveInfo(angle,blockhold,(path.getDistance() - 6.0F),60 ,path.isLine()));
					}
					else
					{
						if(lineflag)
						{
							missionScenario.add(new DriveInfo(angle,blockhold,(path.getDistance()),60 ,path.isLine()));
						}
						else
						{
							missionScenario.add(new DriveInfo(angle,blockhold,(path.getDistance()),60 ,false));
							lineflag = true;
						}
					}
				}
				else
				{
					/*if(correctedDistance != 0.000F)
					{
						if(route.get(i-3).equals(route.get(i)))
						{
							missionScenario.add(new DriveInfo(angle,blockhold,(path.getDistance() + correctedDistance),60 ,path.isLine()));
							correctedDistance = 0.0F;
						}
					}
					else
					{*/
						missionScenario.add(new DriveInfo(angle,blockhold,(path.getDistance()),60 ,path.isLine()));
					//}
				}
				currentAngle = path.getAngle();
				if(currentID > route.get(i+1))
				{
					currentAngle += 180.000F;
				}

				//ズレを考慮したものにする
				
				if(correctedDistance != 0.000F)
				{
					angle = correctedAngle - currentAngle;

					if( (angle > 180.000F) || (angle < -180.000F) )
					{
						if(angle > 0.000F)
						{
							angle -= 360.000F;
						}
						else
						{
							angle += 360.000F;
						}

					}
					currentAngle = correctedAngle;
					//とりあえずspeedは40　後で距離に応じて変えたりするようにする
					missionScenario.add(new DriveInfo(angle,false,-correctedDistance,60,false) );
					correctedDistance = 0.000F;
				}
				
				currentID = route.get(i+1);
			}
		}

	}
}
