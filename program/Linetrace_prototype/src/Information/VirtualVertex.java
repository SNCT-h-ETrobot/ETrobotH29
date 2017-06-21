package Information;

public class VirtualVertex extends Vertex{
	private boolean onLine;	//ライン上にあるか否か

	public VirtualVertex(int pointID,boolean onLine) {
		super(pointID);
		this.onLine = onLine;
	}

	public boolean getOnLine() {
		return onLine;
	}

}
