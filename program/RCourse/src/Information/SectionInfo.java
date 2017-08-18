package Information;

public class SectionInfo {
	private int sectionID;
	private float distance;
	private float kp;
	private float ki;
	private float kd;
	private float targetBrightness;
	private float targetForward;

	public SectionInfo(int sectionID,float distance,float kp,float ki,float kd,float targetBrightness,float targetForward){
		this.sectionID = sectionID;
		this.distance = distance;
		this.kp = kp;
		this.ki = ki;
		this.kd = kd;
		this.targetBrightness = targetBrightness;
		this.targetForward = targetForward;
	}

	public int getSectionID(){
		return this.sectionID;
	}

	public float getDistance(){
		return this.distance;
	}

	public float getKp(){
		return this.kp;
	}

	public float getKi(){
		return this.ki;
	}

	public float getKd(){
		return this.kd;
	}

	public float getTargetBrightness(){
		return this.targetBrightness;
	}

	public float getTargetForward(){
		return this.targetForward;
	}

}
