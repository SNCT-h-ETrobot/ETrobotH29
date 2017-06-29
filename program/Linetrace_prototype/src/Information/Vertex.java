package Information;

public class Vertex {
	public int pointID;
	public float xCoordinate;
	public float yCoordinate;

	public Vertex(int pointID,float x, float y) {
		this.pointID = pointID;
		this.xCoordinate = x;
		this.yCoordinate = y;
	}

	public int getPointID() {
		return pointID;
	}

	public void setPointID(int pointID) {
		this.pointID = pointID;
	}

	public float[] getCoordinates(){
		float[] coordinates = new float[2];
		coordinates[0] = xCoordinate;
		coordinates[1] = yCoordinate;
		return coordinates;
	}
}
