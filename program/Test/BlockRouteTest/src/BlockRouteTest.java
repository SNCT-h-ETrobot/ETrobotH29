import java.util.ArrayList;
import java.util.List;

import Information.BlockArrangeInfo;
import Information.RouteDriver;


public class BlockRouteTest {

	private static final int MAX_CODE = (15-1)*11*11*11 + (11-1)*11*11 + (11-1)*11 + (11-1);
	private static final String[] COLOR_STR = {"K", "R", "G", "B", "Y"};
	private static final int[] BLOCK_L = {151, 0, 90, 4,44};// 左
	private static final int[] BLOCK_R = {151,60,119,12, 8};// 右

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		RouteDriver routeDriver = new RouteDriver();
		BlockArrangeInfo.makeConnection();


		for(int code = 0; code < MAX_CODE; ++code){
			BlockArrangeInfo.setBlockPlace(code);
			int[] blockList = BlockArrangeInfo.getBlockPlaceIDList();
			boolean isSamePlace = false;//ブロックの位置がかぶってるか
			for(int i = 0; i < 4; ++i){
				for (int j = i + 1; j < 5; j++) {
					if(blockList[i] == blockList[j]){
						isSamePlace = true;
						break;
					}
				}
			}
			if(isSamePlace)continue;

			String str = "";
			for(int i = 0; i < blockList.length; ++i){
				str += blockList[i];
				if(blockList.length - 1 != i)str += ", ";
			}
			System.out.println("Decoding Result of " + code+ " : " + str);
			//System.out.println("ID:");
			routeDriver.driveRoute();
			//System.out.println("Route:");
			List<Integer> list = new ArrayList<Integer>();
			list = routeDriver.getRoute();

			int divTgt = 0;
			boolean isHoldBlock = false;
			for(int i = 0;i<list.size();i++){
				if(list.size()-1 == i || (i != 0 && list.get(i).equals(list.get(i-1)))){
					String color = "     ";
					if(isHoldBlock){
						color = "put: ";
						for (int j = 0; j < 5; j++)
							if(list.get(i) == BLOCK_R[j] || list.get(i) == BLOCK_L[j])color = "put:"+COLOR_STR[j];
					}else{
						if(list.get(i).equals(65))
							color = "exit:";
						else
							color = "hold:";
					}
					isHoldBlock = !isHoldBlock;
					if(list.size()-1 == i)
						System.out.println(color + list.subList(divTgt, list.size()));
					else
						System.out.println(color + list.subList(divTgt, i));
					divTgt = i;
				}
			}
		}

		//GamePartDriver GPDriver = new GamePartDriver(1);
		//GPDriver.driveGamePart();
	}

}
