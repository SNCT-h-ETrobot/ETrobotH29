package driveInstruction;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import virtualDevices.ArmController;
import virtualDevices.DistanceMeasure;
import Information.BlockArrangeInfo;
import Information.DriveInfo;
import Information.Path;
import Information.RouteDriver;
import driveControl.DistanceAngleController;
import driveControl.Linetracer;

public class GamePartDriver {

	private float currentAngle;
	private int currentID;
	private ArrayList<DriveInfo> missionScenario;

	private ArmController arm;
	private Linetracer linetracer;
	private DistanceMeasure distMeasure;
	private DistanceAngleController dACtrl;
	private RouteDriver routeDriver;

	private final float P_GAIN = 50.0F;	//P係数
	private final float I_GAIN = 10.0F;	//I係数
	private final float D_GAIN = 5.0F;	//D係数

	private final float TARGET_BRIGHTNESS = 0.5F;//要調整もっと高いかも
	
	private float blockMoveCorrection = 0.0F;
	private final float LINETRACE_ANGLE_CONNECTION = 7.0F;
	private final float BLOCK_MOVE_DISTANCE = 5.0F;

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
			//ブロック並べに突入するときの進行方向は右上方向なので315度
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
		createMissionScenario(routeDriver.getRoute());

		LCD.drawString("Size:" + missionScenario.size(), 0, 0);
		Delay.msDelay(3000);

		float distance;
		float speed;
		arm.controlArmNormalAngel();
		for(int i=0;i<missionScenario.size();i++)
		{

			//LCD.drawString("turn:"+missionScenario.get(i).getTurnAngle(), 0, 1);


			if(missionScenario.get(i).getHoldBlock())
			{
				if(missionScenario.get(i).getTurnAngle() == 180.0F || missionScenario.get(i).getTurnAngle() == -180.0F)
				{
					//r=12.6cm,a=65°
					dACtrl.TurningControl((float) (2.0F * Math.PI * 12.6F * 65.0F / 360.0F), 60, false);
					dACtrl.TurningControl((float) (2.0F * Math.PI * 12.6F * ((65.0F*2.0F + 180.0F) / 360.0F)), 60, true);
					dACtrl.TurningControl((float) (2.0F * Math.PI * 12.6F * 65.0F / 360.0F), 60, false);
				}
				else if(missionScenario.get(i).getTurnAngle() == 0.000F)
				{
					if(missionScenario.get(i).getLinetrace()){//次がライントレースの時に右側よりにする 
						dACtrl.turn(LINETRACE_ANGLE_CONNECTION + 3.0F,true);
					}
				}
				else
				{
					if(missionScenario.get(i).getLinetrace()){//次がライントレースの時に右側よりにする
						//右回りの時は少し強め 左回りの時は少し弱め
						float theta = missionScenario.get(i).getTurnAngle()/2.0F + LINETRACE_ANGLE_CONNECTION / 2.0F;
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

			if(missionScenario.get(i).getLinetrace()){
				dACtrl.goStraightAhead(distance/4.0F,speed);
				//1/4～3/4の区間の間だけライントレース
				distMeasure.resetDistance();
				while(distMeasure.getDistance()<distance/2.0F - blockMoveCorrection){//ブロックの分微調整
					linetracer.linetrace(P_GAIN, I_GAIN, D_GAIN, TARGET_BRIGHTNESS, speed);
					Delay.msDelay(4);
				}
				dACtrl.goStraightAhead(distance/4.0F,speed);
				blockMoveCorrection = 0.0F;
			}

			else{
				//LCD.drawString("distance:"+missionScenario.get(i).getDistance(), 0, 2);
				dACtrl.goStraightAhead(distance,speed);
			}
		}

	}

	private void createMissionScenario(List<Integer> route)
	{
		//頂点をどのような順番で通るかにより、各値を求めてリストに格納

		float correctedAngle = 0;
		float correctedDistance = 0;
		boolean blockhold = false;

		for(int i=1;i<route.size();i+=2)
		{
			//目的地に到着
			if(route.get(i).equals(route.get(i-1)))
			{
				//ブロックを置く処理
				if(blockhold)
				{
					missionScenario.add(new DriveInfo(0.000F,false,-10.000F,60,false) );
					correctedAngle = currentAngle;
					correctedDistance = -10.000F;
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
				//とりあえずspeedは40　後で距離に応じて変えたりするようにする
				missionScenario.add(new DriveInfo(angle,blockhold,(path.getDistance()),60 ,path.isLine()));


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
