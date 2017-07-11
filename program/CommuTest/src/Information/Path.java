package Information;

public class Path {
	private int pointID;
	private float distance;
	private int angle;
	private boolean isLine;	//実線かどうか

	public Path(int pointID, float distance, int angle, boolean isLine) {
		this.pointID = pointID;
		this.distance = distance;
		this.angle = angle;
		this.isLine = isLine;
	}
	public int getPointID() {
		return pointID;
	}

	public float getDistance() {
		return distance;
	}

	public int getAngle() {
		return angle;
	}

	public boolean isLine() {
		return isLine;
	}


}
