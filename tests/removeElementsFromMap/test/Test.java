import gool.imports.java.util.HashMap;

public class Test {
	public static void main(String[] args) {
		HashMap<Integer, Integer> m = new HashMap<Integer, Integer>();
		m.put(1, 2);
		m.put(2, 3);
		m.remove(2);
		System.out.println(m.size());
	}
}
