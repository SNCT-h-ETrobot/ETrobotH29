
public class Decode {
	static private final int GREEN_PLACE_ID = 0; //仮の番号。当日打ち込む
	static private final int K_BLACK = 1331; //11*11*11
	static private final int K_RED = 121; //11*11
	static private final int K_YELLOW = 11; //11
	static private final int K_BLUE = 1;
	static private int[] blockPlaceIDList = new int[5];

	public static void setBlockPlace(int code){
		int n = code;
		int i = 1;

		blockPlaceIDList[2] = GREEN_PLACE_ID;

		i = (int)(n/K_BLACK);
		n = n - (i*K_BLACK);
		switch(i+1){
			case 1:
				blockPlaceIDList[0] = 0;
				break;
			case 2:
				blockPlaceIDList[0] = 4;
				break;
			case 3:
				blockPlaceIDList[0] = 8;
				break;
			case 4:
				blockPlaceIDList[0] = 12;
				break;
			case 5:
				blockPlaceIDList[0] = 44;
				break;
			case 6:
				blockPlaceIDList[0] = 52;
				break;
			case 7:
				blockPlaceIDList[0] = 60;
				break;
			case 8:
				blockPlaceIDList[0] = 74;
				break;
			case 9:
				blockPlaceIDList[0] = 81;
				break;
			case 10:
				blockPlaceIDList[0] = 90;
				break;
			case 11:
				blockPlaceIDList[0] = 119;
				break;
			case 12:
				blockPlaceIDList[0] = 121;
				break;
			case 13:
				blockPlaceIDList[0] = 128;
				break;
			case 14:
				blockPlaceIDList[0] = 132;
				break;
			case 15:
				blockPlaceIDList[0] = 139;
				break;
		}

		i = (int)(n/K_RED);
		n = n - (i*K_RED);
		switch(i+1){
			case 1:
				blockPlaceIDList[1] = 4;
				break;
			case 2:
				blockPlaceIDList[1] = 8;
				break;
			case 3:
				blockPlaceIDList[1] = 12;
				break;
			case 4:
				blockPlaceIDList[1] = 44;
				break;
			case 5:
				blockPlaceIDList[1] = 52;
				break;
			case 6:
				blockPlaceIDList[1] = 81;
				break;
			case 7:
				blockPlaceIDList[1] = 90;
				break;
			case 8:
				blockPlaceIDList[1] = 119;
				break;
			case 9:
				blockPlaceIDList[1] = 121;
				break;
			case 10:
				blockPlaceIDList[1] = 128;
				break;
			case 11:
				blockPlaceIDList[1] = 139;
				break;
		}

		i = (int)(n/K_YELLOW);
		n = n - (i*K_YELLOW);
		switch(i+1){
			case 1:
				blockPlaceIDList[4] = 0;
				break;
			case 2:
				blockPlaceIDList[4] = 4;
				break;
			case 3:
				blockPlaceIDList[4] = 12;
				break;
			case 4:
				blockPlaceIDList[4] = 52;
				break;
			case 5:
				blockPlaceIDList[4] = 60;
				break;
			case 6:
				blockPlaceIDList[4] = 74;
				break;
			case 7:
				blockPlaceIDList[4] = 81;
				break;
			case 8:
				blockPlaceIDList[4] = 90;
				break;
			case 9:
				blockPlaceIDList[4] = 119;
				break;
			case 10:
				blockPlaceIDList[4] = 121;
				break;
			case 11:
				blockPlaceIDList[4] = 132;
				break;
		}

		i = (int)(n/K_BLUE);
		n = n - (i*K_BLUE);
		switch(i+1){
			case 1:
				blockPlaceIDList[3] = 0;
				break;
			case 2:
				blockPlaceIDList[3] = 8;
				break;
			case 3:
				blockPlaceIDList[3] = 44;
				break;
			case 4:
				blockPlaceIDList[3] = 52;
				break;
			case 5:
				blockPlaceIDList[3] = 60;
				break;
			case 6:
				blockPlaceIDList[3] = 74;
				break;
			case 7:
				blockPlaceIDList[3] = 90;
				break;
			case 8:
				blockPlaceIDList[3] = 119;
				break;
			case 9:
				blockPlaceIDList[3] = 128;
				break;
			case 10:
				blockPlaceIDList[3] = 132;
				break;
			case 11:
				blockPlaceIDList[3] = 139;
				break;
		}
	}

	public static int[] getResult(){
		return blockPlaceIDList;
	}
}
