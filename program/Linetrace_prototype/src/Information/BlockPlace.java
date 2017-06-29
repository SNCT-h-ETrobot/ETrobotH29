package Information;

public class BlockPlace extends Vertex{
	private int colorID;	//黒0,赤1,緑2,青3,黄4

	public BlockPlace(int pointID,float x,float y, int colorID) {
		super(pointID,x,y);
		this.colorID = colorID;
	}

	public int getColorID() {
		return colorID;
	}

	public void setColorID(int colorID) {
		this.colorID = colorID;
	}

}
