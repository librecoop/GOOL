import gool.imports.java.util.HashMap;

public class Test {
	public static void main(String[] args) {
		String four = "four";
		HashMap<String, Integer > m = new HashMap<String, Integer>();
		m.put(four,4);
		System.out.println(m.get(four));
	}
}
