package driveInstruction;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import sectionRun.SectionRun;
/*import Information.BlockArrangeInfo;
import Information.DriveInfo;
import Information.Path;
import Information.RouteDriver;
*/
import Information.SectionRunScenario;
import driveControl.DistanceAngleController;
import driveControl.WheelController;
public class GamePartDriver {
	private int courseID;

	private float currentAngle;
	private int currentID;
	//名前をシナリオかinfoに統一する
	//private ArrayList<DriveInfo> missionScenario;
	private WheelController wheelCtrl;

	private DistanceAngleController dACtrl;
	//private BlockArrangeInfo blockArrangeInfo;
	//private RouteDriver routeDriver;

	private SectionRunScenario sectionScenario;

	public GamePartDriver(int courseID)
	{
		this.courseID = courseID;
		dACtrl = new DistanceAngleController();
		//Lコース
		if(courseID == 1)
		{
		}
		//Rコース
		else if(courseID == 2)
		{
			sectionScenario = new SectionRunScenario();
		}

	}

	public void driveGamePart()
	{
		if(courseID ==1){

		}
		else if(courseID == 2){
			SectionRun[] section = sectionScenario.getScenario();
			
			for(int i = 0; i < section.length; i++){
				section[i].run();
			}
		}

	}

/*	private void createMissionScenario(List<Integer> route)
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
					missionScenario.add(new DriveInfo(0.000F,false,-10.000F,40) );
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
				System.out.println("i:" +i+ " "+ currentID + "->" + route.get(i+1) + " angle:"+angle);
				//とりあえずspeedは40　後で距離に応じて変えたりするようにする
				missionScenario.add(new DriveInfo(angle,blockhold,(path.getDistance()),40 ));


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
					//とりあえずspeedは40　後で距離に応じて変えたりするようにする
					missionScenario.add(new DriveInfo(angle,false,-correctedDistance,40) );
					correctedDistance = 0.000F;
				}
				currentID = route.get(i+1);
			}
		}
	}
*/
}
