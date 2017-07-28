package Information;

public class DriveInfo {

	private float turnAngle;
	private boolean holdBlock;
	private float distance;
	private int speed;
	private boolean linetrace;

	public DriveInfo(float turnAngle, boolean holdBlock, float distance, int speed,boolean linetrace)
	{
		this.turnAngle = turnAngle;
		this.holdBlock = holdBlock;
		this.distance = distance;
		this.speed = speed;
		this.linetrace = linetrace;
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

}
