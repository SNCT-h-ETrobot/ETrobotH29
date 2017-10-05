package Information;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteDriver {

	private static List<Integer> routeList = new ArrayList<Integer>();// ルート計算の結果
	private final float DEGRESS_MULT = 0;// 角度を距離に直すときの倍率
	private static final float[] COST_MULT = {0.001f,1.0f,1.0f,1.0f,1.0f};//コストに補正をかける，小さいほど動かす対象に選ばれやすい・黒、赤、緑、青、黄の順

	public void driveRoute(){
		//右と左のルートとそれぞれのルートのコスト
		List<Integer> tempList = new ArrayList<Integer>();
		List<Integer> tempList2 = new ArrayList<Integer>();
		float routeCost = 0,routeCost2 = 0;
		int[][] blackPlaceJudge = new int[][]{ {52,74,81,128,132,121},{0,0,0,0,0,0} };

		for (int i = 0; i < 2; i++) {
			int blockCarryTarget[];// ブロックを置く目標位置  黒、赤、緑、青、黄の順
			if(i == 0) blockCarryTarget = new int[]{52, 0, 90, 4,44};// 左
			else       blockCarryTarget = new int[]{52,60,119,12, 8};// 右

			int robotPlace = 90; // 走行体の位置 初期値は走行区間から続く緑のブロック置き場
			//一回ごとにリストとコストをリセット
			tempList.clear();
			routeCost = 0;

			int[] blockPlace = BlockArrangeInfo.getBlockPlaceIDList();// ブロックの置いてある位置
			
			//黒ブロックの目標位置をブロックが無いところにする
			for(int j = 0; j < 6; j++)
			{
				for(int k = 0; k < blockPlace.length;k++)
				{
					if(blackPlaceJudge[0][j] == blockPlace[k])
					{
						blackPlaceJudge[1][j]++;
						break;
					}
					
				}
				if(blackPlaceJudge[1][j] == 0)
				{
					blockCarryTarget[0] = blackPlaceJudge[0][j];
					break;
				}
			}
			
			
			for (int j = 0; j < 5; j++) {
				// 移動先が開いているブロックの色を調べる
				boolean[] canCarry = new boolean[5];
				boolean canNotCarryAll = true;
				for (int k = 0; k < 5; k++) {
					// それぞれのブロックに対して、ブロックの移動先にいずれかのブロックが置いてあるなら運べない
					canCarry[k] = true;
					for (int k2 = 0; k2 < 5; k2++) {
						if(blockCarryTarget[k] == blockPlace[k2]){
							canCarry[k] = false;
							break;
						}
					}
					if(canCarry[k] == true)canNotCarryAll = false;
				}

				// 動かせないときの処理
				if(canNotCarryAll){
					// 経路探索して最小コストを探索
					float lowestCost = Float.MAX_VALUE;
					List<Integer> lowestRoute = null;
					int lowestColor = 0;
					for (int k = 0; k < 5; k++) {
						// すでに置いた場所をターゲットとすることがないように
						if(blockCarryTarget[k] == blockPlace[k])continue;
						//同一地点をターゲットとすることがないように
						if(robotPlace != blockPlace[k]){
							List<Integer> tempRoute = aStar(robotPlace, blockPlace[k], blockPlace, false);
							float tempCost = calcCost(tempRoute);
							if(lowestCost > tempCost){
								lowestCost = tempCost;
								lowestRoute = tempRoute;
								lowestColor = k;
							}
						}
					}
					// エラー落ち回避用
					if(lowestRoute == null){
						lowestRoute = new ArrayList<Integer>();
						lowestRoute.add(robotPlace);
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

					List<Integer> tempRoute = aStar(place, blockTempTarget, blockPlace, true);
					float tempCost = calcCost(tempRoute);
					tempList.addAll(tempRoute);
					routeCost += tempCost;

					// 走行体とブロックの位置を更新
					robotPlace = blockPlace[lowestColor] = blockTempTarget;

					// 移動先が開いているブロックの色を再度調べる
					for (int k = 0; k < 5; k++) {
						canCarry[k] = true;
						for (int k2 = 0; k2 < 5; k2++) {
							if(blockCarryTarget[k] == blockPlace[k2]){
								canCarry[k] = false;
								break;
							}
						}
					}


				}

				// 経路探索して最小コストを探索
				float lowestCost = Float.MAX_VALUE;
				List<Integer> lowestRoute = null;
				int lowestColor = 0;
				for (int k = 0; k < 5; k++) {
					if(!canCarry[k])continue;
					//System.out.println(robotPlace + "->" + blockPlace[k]);
					//同一地点をターゲットとすることがないように
					if(robotPlace != blockPlace[k]){
						List<Integer> tempRoute = aStar(robotPlace, blockPlace[k],blockPlace, false);
						float tempCost = calcCost(tempRoute) * COST_MULT[k];
						if(lowestCost > tempCost){
							lowestCost = tempCost;
							lowestRoute = tempRoute;
							lowestColor = k;
						}
					}
				}
				// エラー落ち回避用
				if(lowestRoute == null){
					lowestRoute = new ArrayList<Integer>();
					lowestRoute.add(robotPlace);
				}
				tempList.addAll(lowestRoute);
				routeCost += lowestCost;

				// ブロックから目標位置まで
				//同一地点をターゲットとすることがないように
				if(blockPlace[lowestColor] != blockCarryTarget[lowestColor]){
					List<Integer> tempRoute = aStar(blockPlace[lowestColor], blockCarryTarget[lowestColor],blockPlace, true);
					float tempCost = calcCost(tempRoute);
					tempList.addAll(tempRoute);
					routeCost += tempCost;
				}

				// 走行体とブロックの位置を更新
				robotPlace = blockPlace[lowestColor] = blockCarryTarget[lowestColor];
			}

			// 脱出地点に向かう
			List<Integer> tempRoute = aStar(robotPlace, 65, blockPlace, true);
			float tempCost = calcCost(tempRoute);
			tempList.addAll(tempRoute);
			routeCost += tempCost;


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

	private List<Integer> aStar(int startID, int targetID, int[] blockPlace ,boolean isCarry){
		List<Integer> route = new ArrayList<Integer>();
		List<Integer> openList = new ArrayList<Integer>();
		List<Integer> closeList = new ArrayList<Integer>();

		//System.out.println(startID+"to"+targetID);
		//コスト
		double[] f = new double[152];
		for(int i = 0;i<150;i++){
			f[i] = 0.0F;
		}

		//子のIDがKey
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();

		int selectID;

		openList.add(startID);
		Vertex target = new Vertex(0,0,0);
		target = (Vertex)BlockArrangeInfo.getPointObject(targetID);
		float[] coordinateTarget = new float[2];
		coordinateTarget = target.getCoordinates();
		while(true){
			//解なし
			if(openList.isEmpty()){
				//System.out.println("Routing Fault");
				break;
			}

			//最小コストを選択
			selectID = openList.get(0);
			openList.remove(0);

			//ゴールなら終了
			if(selectID == targetID){
				break;
			}
			else{
				closeList.add(selectID);
			}

			//選択した箇所と隣接してるやつのコストに応じて処理
			List<Vertex> tempVertexList = new ArrayList<Vertex>(BlockArrangeInfo.getConnectionVertex(selectID));

			//目的ではないブロックで埋まってる場合除外
			int k = 0;
			while(tempVertexList.size() > k){
				for(int j = 0;j < 5;j++){
					if(tempVertexList.get(k).getPointID() == targetID){
						//目的地の場合何もしない
					}
					else if(tempVertexList.get(k).getPointID() == blockPlace[j]){
						//そうではないときは選択肢から外す
						tempVertexList.remove(k);
						k = 0;
						break;
					}
					if(j == 4){
						k++;
					}
				}
			}

			//親までのrouteのList
			List<Integer> tempRoute = new ArrayList<Integer>();
			tempRoute.add(0, selectID);
			while(true){
				if(tempRoute.get(0) == startID){
					break;
				}
				tempRoute.add(0,BlockArrangeInfo.getConnectionPath(tempRoute.get(0), map.get(tempRoute.get(0))).getPointID());
				tempRoute.add(0, map.get(tempRoute.get(1)));
			}

			//親の概算コスト
			Vertex parent = (Vertex) BlockArrangeInfo.getPointObject(tempRoute.get(tempRoute.size()-1));
			float[] coordinatesPar = parent.getCoordinates();
			double hn = Math.sqrt(Math.pow(coordinatesPar[0]-coordinateTarget[0],2.0F)+Math.pow(coordinatesPar[1]-coordinateTarget[1],2.0F));

			for(int i = 0;i<tempVertexList.size();i++){
				float[] coordinates = tempVertexList.get(i).getCoordinates();
				
				double fdmult;
				if(BlockArrangeInfo.getConnectionPath(selectID,tempVertexList.get(i).getPointID()).isLine())
					fdmult = 1.0;
				else
					fdmult = 10.0;
				
				//f'=g+cost+h
				//半ば簡易的。calcCostとは結果が違う
				double fd = (f[selectID] - hn)
						+BlockArrangeInfo.getConnectionPath(selectID,tempVertexList.get(i).getPointID()).getDistance() * fdmult
						+Math.sqrt(Math.pow(coordinates[0]-coordinateTarget[0],2.0F)+Math.pow(coordinates[1]-coordinateTarget[1],2.0F));

				//オープンされている
				if(openList.contains(tempVertexList.get(i).getPointID())){
					if(fd<f[tempVertexList.get(i).getPointID()]){
						f[tempVertexList.get(i).getPointID()] = fd;
						map.put(tempVertexList.get(i).getPointID(), selectID);
					}
				}
				//クローズされている
				else if(closeList.contains(tempVertexList.get(i).getPointID())){
					if(fd<f[tempVertexList.get(i).getPointID()]){
						f[tempVertexList.get(i).getPointID()] = fd;
						closeList.remove(tempVertexList.get(i).getPointID());
						openList.add(tempVertexList.get(i).getPointID());
						map.put(tempVertexList.get(i).getPointID(), selectID);
					}
				}
				//どちらでもない場合
				else{
					f[tempVertexList.get(i).getPointID()] = fd;
					openList.add(tempVertexList.get(i).getPointID());
					map.put(tempVertexList.get(i).getPointID(), selectID);
				}
			}

			//openListはindexが先頭に行くほどコストが低い
			//バブルなので遅かったら変える
			for(int i = 0; i < openList.size()-1; i++){
				for(int j = openList.size()-1; j > i ; j--){
					//コスト比較
					if(f[openList.get(j)] <f[openList.get(j-1)]){
						int temp = openList.get(j);
						openList.set(j, openList.get(j-1));
						openList.set(j-1, temp);
					}
				}
			}
		}

		//System.out.println("routing");
		//route生成
		route.add(0,targetID);
		do{
			//System.out.println(route.get(0));
			route.add(0,BlockArrangeInfo.getConnectionPath(route.get(0),map.get(route.get(0))).getPointID());
			//System.out.println(route.get(0));
			route.add(0,map.get(route.get(1)));
		}while(route.get(0)!=startID);
		//System.out.println(route.get(0));

		return route;
	}

	// ルートのリストを渡すとコストが返ってくるメソッド
	private float calcCost(List<Integer> route){
		float cost = 0;
		int[] array = new int[route.size()];
		List<Path> tempPathList;
		float preAngle = 0.0F;
		float nextAngle = 0.0F;
		//頂点、エッジ、頂点、エッジ……と並ぶと想定
		for(int i = 0; i<route.size();i++){
			//何度もアクセスすると遅いので一度Listから出す
			//メモリ足りなくなるなら辞め
			array[i] = route.get(i);
		}

		for(int i = 0; i<route.size()-2;i=i+2){
			tempPathList = new ArrayList<Path>(BlockArrangeInfo.getConnectionPath(array[i]));
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
