package Information;

public class VirtualVertex extends Vertex{
	private boolean onLine;	//ライン上にあるか否か

	public VirtualVertex(int pointID,float x,float y,boolean onLine) {
		super(pointID,x,y);
		this.onLine = onLine;
	}

	public boolean getOnLine() {
		return onLine;
	}

}
