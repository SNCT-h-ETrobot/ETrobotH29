package Information;

public class DriveInfo {

	private float turnAngle;
	private boolean holdBlock;
	private float distance;
	private int speed;
	private boolean linetrace;
	private int colorID;

	public DriveInfo(float turnAngle, boolean holdBlock, float distance, int speed,boolean linetrace,int colorID)
	{
		this.turnAngle = turnAngle;
		this.holdBlock = holdBlock;
		this.distance = distance;
		this.speed = speed;
		this.linetrace = linetrace;
		this.colorID = colorID;
	}

	public float getTurnAngle()
	{
		return turnAngle;
	}

	public boolean getHoldBlock()
	{
		return holdBlock;
	}

	public float getDistance()
	{
		return distance;
	}

	public int getSpeed()
	{
		return speed;
	}

	public boolean getLinetrace()
	{
		return linetrace;
	}
	
	public int getColorID()
	{
		return colorID;
	}

}
