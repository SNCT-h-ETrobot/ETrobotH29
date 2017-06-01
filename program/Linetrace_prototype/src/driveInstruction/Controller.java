package driveInstruction;

public class Controller {	//クラス名は再考が必要

	static RacePartDriver RPdriver;

	private int courseID;

	public Controller(){
		//Lコース
		courseID = 1;

		//Rコース
		//courseID = 2;
		RPdriver = new RacePartDriver(courseID);
	}

	public void drive(){
		RPdriver.driveRacePart();
		//以後ゲー区間
	}
}
