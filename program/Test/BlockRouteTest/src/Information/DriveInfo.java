package Information;

public class DriveInfo {
	
	private float turnAngle;
	private boolean holdBlock;
	private float distance;
	private int speed;
	
	public DriveInfo(float turnAngle, boolean holdBlock, float distance, int speed)
	{
		this.turnAngle = turnAngle;
		this.holdBlock = holdBlock;
		this.distance = distance;
		this.speed = speed;
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

}
