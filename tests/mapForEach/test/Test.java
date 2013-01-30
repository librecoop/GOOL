import gool.imports.java.util.HashMap;

public class Test {
	public static void main(String[] args) {
		Integer total = 0;
		HashMap<Integer, Integer> m = new HashMap<Integer, Integer>();
		m.put(0, 1); m.put(2, 3);
		for(HashMap.Entry<Integer, Integer> entry : m){
			total = total + entry.getKey();
			total = total + entry.getValue();
		}
		System.out.println(total);
	}
}
