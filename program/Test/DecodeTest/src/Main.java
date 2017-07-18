
public class Main {

	public static void main(String[] args) {
		// デコードのテスト
		int code = 12008; //競技規約の例。黒90,赤4,青119,黄12
		Decode.setBlockPlace(code);
		int[] array = Decode.getResult();
		for(int i = 0; i < 5; i++){
			System.out.println(array[i]);
		}

	}

}
