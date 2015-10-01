import java.util.Map;
import java.util.HashMap;

public class test{
	public static void main(String[] args){
		/* Test containsKey method translation */
		Map<Integer,Integer> m = new HashMap<Integer,Integer>();
		m.put(1,2);
		if(m.containsKey(1)){
			System.out.println("true");
		}
		else{
			System.out.println("false");
		}
	}
}
