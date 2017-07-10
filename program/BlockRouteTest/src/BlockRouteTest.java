import java.util.ArrayList;
import java.util.List;

import Information.BlockArrangeInfo;
import Information.RouteDriver;


public class BlockRouteTest {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		RouteDriver routeDriver = new RouteDriver();
		BlockArrangeInfo.makeConnection();
		int code;
		int[] blockPlace = {1,1,1,1}; //初期値。競技規約に対応。BRYB
		while(blockPlace[0] <= 15){//Bが範囲外になるまで
			while(blockPlace[1] <= 11){//Rが範囲外になるまで
				while(blockPlace[2] <= 11){//Yが範囲外になるまで
					while(blockPlace[3] <= 11){//Bが範囲外になるまで
						code = (blockPlace[0]-1)*11*11*11
								+(blockPlace[1]-1)*11*11
								+(blockPlace[2]-1)*11
								+(blockPlace[3]-1);
						BlockArrangeInfo.setBlockPlace(code);
						System.out.println("Decoding Result of "+code);
						int[] decodeResult = BlockArrangeInfo.getBlockPlaceIDList();
						for(int i = 0; i < 5;i++){
							System.out.println(decodeResult[i]);
						}
						System.out.println("ID:");
						routeDriver.deriveRoute();
						List<Integer> list = new ArrayList<Integer>();
						for(int i = 0;i<list.size();i++){
							System.out.println(list.get(i));
						}
						blockPlace[3]++;
					}
					blockPlace[2]++;
				}
				blockPlace[1]++;
			}
			blockPlace[0]++;
		}
	}

}
