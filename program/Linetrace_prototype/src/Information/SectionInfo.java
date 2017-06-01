package Information;

public class SectionInfo {
	private int sectionID;
	private float distance;
	private float kp;
	private float ki;
	private float kd;
	private float target_brightness;
	private float target_forward;

	public SectionInfo(int sectionID,float distance,float kp,float ki,float kd,float target_brightness,float target_forward){
		this.sectionID = sectionID;
		this.distance = distance;
		this.kp = kp;
		this.ki = ki;
		this.kd = kd;
		this.target_brightness = target_brightness;
		this.target_forward = target_forward;
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
		return this.target_brightness;
	}

	public float getTargetForward(){
		return this.target_forward;
	}

}
