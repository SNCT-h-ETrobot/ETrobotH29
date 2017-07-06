package Information;
import java.util.ArrayList;
import java.util.List;

public class RouteDriver {

	private List<Integer> routeList = new ArrayList<Integer>();// ルート計算の結果
	private final float DEGRESS_MULT = 0;// 角度を距離に直すときの倍率

	public void deriveRoute(){
		//右と左のルートとそれぞれのルートのコスト
		List<Integer> tempList = new ArrayList<Integer>();
		List<Integer> tempList2 = new ArrayList<Integer>();
		int routeCost = 0,routeCost2 = 0;


		for (int i = 0; i < 2; i++) {
			int blockCarryTarget[];// ブロックを置く目標位置
			if(i == 0) blockCarryTarget = new int[]{150, 0, 90, 4,44};// 左
			else       blockCarryTarget = new int[]{150,66,119,12, 8};// 右

			int robotPlace = 90; // 走行体の位置 初期値は走行区間から続く緑のブロック置き場
			//一回ごとにリストとコストをリセット
			tempList.clear();
			routeCost = 0;

			int[] blockPlace = BlockArrangeInfo.getBlockPlaceIDList();
			for (int j = 0; j < 5; j++) {

				// 移動先が開いているブロックの色を調べる
				boolean[] canCarry = new boolean[5];
				boolean canNotCarryAll = true;
				for (int k = 0; k < 5; k++) {
					canCarry[k] = true;
					for (int k2 = 0; k2 < 5; k2++) {
						if(blockCarryTarget[k] == blockPlace[k2]){
							canCarry[k] = false;
							break;
						}
					}
					if(canCarry[k] = true)canNotCarryAll = false;

				}

				// 動かせないときの処理
				if(canNotCarryAll){
					// 経路探索して最小コストを探索
					int lowestCost = Integer.MAX_VALUE;
					List<Integer> lowestRoute = null;
					int lowestColor = 0;
					for (int k = 0; k < 5; k++) {
						List<Integer> tempRoute = aStar(robotPlace, blockPlace[k], false);
						int tempCost = calcCost(tempRoute);
						if(lowestCost > tempCost){
							lowestCost = tempCost;
							lowestRoute = tempRoute;
							lowestColor = k;
						}
					}
					tempList.addAll(lowestRoute);
					routeCost += lowestCost;

					// 最小コストのブロックを近くて関係ない位置まで移動
					// ブロック位置は必ず目標位置とかぶるので移動先はブロック位置によって一意に決めます
					int place = blockPlace[lowestColor];
					int blockTempTarget = place ==  0 ? 74
										: place == 44 ? 74
										: place == 90 ? 121
										: place ==  4 ? 52
										: place == 12 ? 81
										: place == 66 ? 81
										: place ==119 ? 139
										: place ==  8 ? 52
													  : 52;

					List<Integer> tempRoute = aStar(blockPlace[lowestColor], blockTempTarget, true);
					int tempCost = calcCost(tempRoute);
					tempList.addAll(tempRoute);
					routeCost += tempCost;

					// 走行体とブロックの位置を更新
					blockPlace[lowestColor] = blockTempTarget;
					robotPlace = blockTempTarget;

				}

				// 経路探索して最小コストを探索
				int lowestCost = Integer.MAX_VALUE;
				List<Integer> lowestRoute = null;
				int lowestColor = 0;
				for (int k = 0; k < 5; k++) {
					if(!canCarry[k])continue;
					List<Integer> tempRoute = aStar(robotPlace, blockPlace[k], false);
					int tempCost = calcCost(tempRoute);
					if(lowestCost > tempCost){
						lowestCost = tempCost;
						lowestRoute = tempRoute;
						lowestColor = k;
					}
				}
				tempList.addAll(lowestRoute);
				routeCost += lowestCost;

				// ブロックから目標位置まで
				List<Integer> tempRoute = aStar(blockPlace[lowestColor], blockCarryTarget[lowestColor], true);
				int tempCost = calcCost(tempRoute);
				tempList.addAll(tempRoute);
				routeCost += tempCost;

				// 走行体とブロックの位置を更新
				blockPlace[lowestColor] = blockCarryTarget[lowestColor];
				robotPlace = blockCarryTarget[lowestColor];
			}

			// 最初に計算したルートとコストを保存
			if(i==0){
				tempList2 = new ArrayList<Integer>(tempList);
				routeCost2 = routeCost;
			}

		}
		// 右と左でどちらがコスト低いか比較する
		// 低い方を変数に保存する ・ あとでgetRouteで参照する
		if(routeCost < routeCost2){
			routeList = new ArrayList<Integer>(tempList);
		}
		else{
			routeList = new ArrayList<Integer>(tempList2);
		}
	}

	private List<Integer> aStar(int startID, int targetID, boolean isCarry){
		List<Integer> list = new ArrayList<Integer>();

		return list;
	}

	// ルートのリストを渡すとコストが返ってくるメソッド
	private int calcCost(List<Integer> route){
		int cost = 0;
		int[] array = new int[route.size()];
		List<Path> tempPathList = new ArrayList<Path>();
		int preAngle = 0;
		int nextAngle = 0;
		//頂点、エッジ、頂点、エッジ……と並ぶと想定
		for(int i = 0; i<route.size();i++){
			//何度もアクセスすると遅いので一度Listから出す
			//メモリ足りなくなるなら辞め
			array[i] = route.get(i);
		}

		for(int i = 0; i<route.size()-2;i=i+2){
			tempPathList = BlockArrangeInfo.getConnectionPath(array[i]);
			for(int j = 0;j<tempPathList.size();j++){
				if(tempPathList.get(j).getPointID() == array[i+1]){
					if(array[i] < array[i+2]){
						nextAngle = tempPathList.get(j).getAngle();
					}
					else{
						nextAngle = tempPathList.get(j).getAngle() * -1;
					}

					if(tempPathList.get(j).getAngle() > 180){
						cost += tempPathList.get(j).getDistance()
								+Math.abs(preAngle - (nextAngle-180))*DEGRESS_MULT;
					}
					else{
						cost += tempPathList.get(j).getDistance()
								+Math.abs(preAngle - nextAngle)*DEGRESS_MULT;
					}
					preAngle = nextAngle;
				}
			}
		}

		return cost;
	}

	// 算出したルートのgetter
	public List<Integer> getRoute(){
		return routeList;
	}

}
