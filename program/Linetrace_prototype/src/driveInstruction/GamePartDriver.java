package driveInstruction;

import java.util.ArrayList;
import java.util.List;

import Information.BlockArrangeInfo;
import Information.DriveInfo;
import Information.Path;
import Information.RouteDriver;
import driveControl.DistanceAngleController;

public class GamePartDriver {

	private float currentAngle;
	private int currentID;
	//名前をシナリオかinfoに統一する
	private ArrayList<DriveInfo> missionScenario;

	private DistanceAngleController dACtrl;
	//private BlockArrangeInfo blockArrangeInfo;
	private RouteDriver routeDriver;

	public GamePartDriver(int courceID)
	{
		dACtrl = new DistanceAngleController();
		missionScenario = new ArrayList<DriveInfo>();
		routeDriver = new RouteDriver();

		//Lコース
		if(courceID == 1)
		{
			//ブロック並べに突入するときの進行方向は右上方向なので315度
			currentAngle = 315.000F;
			currentID = 90;
			//blockArrangeInfo = new BlockArrangeInfo();
		}
		//Rコース
		else if(courceID == 2)
		{

		}

	}

	public void driveGamePart()
	{
		createMissionScenario(routeDriver.getRoute());
		for(int i=0;i<missionScenario.size();i++)
		{
		  dACtrl.Turn(missionScenario.get(i).getTurnAngle(),missionScenario.get(i).getHoldBlock());
		  dACtrl.GoStraightAhead(missionScenario.get(i).getDistance(),missionScenario.get(i).getSpeed());
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
			if(route.get(i) == route.get(i-1))
			{
				//ブロックを置く処理
				if(blockhold)
				{
					missionScenario.add(new DriveInfo(0.000F,false,-10.000F,40) );
					correctedAngle = currentAngle;
					correctedDistance = -10.000F;
				}
				i--;
				blockhold = !blockhold;
			}
			else
			{
				Path path = (Path)BlockArrangeInfo.getPointObject(i);

				float angle = path.getAngle() - currentAngle;

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

				//とりあえずspeedは40　後で距離に応じて変えたりするようにする
				missionScenario.add(new DriveInfo(angle,blockhold,(path.getDistance()),40 ));


				currentAngle = path.getAngle();

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
					//とりあえずspeedは40　後で距離に応じて変えたりするようにする
					missionScenario.add(new DriveInfo(angle,false,-correctedDistance,40) );
					currentAngle = path.getAngle();
					correctedDistance = 0.000F;
				}
			}
		}

	}
}
