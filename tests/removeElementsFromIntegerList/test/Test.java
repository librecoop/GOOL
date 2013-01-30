import gool.imports.java.util.ArrayList;

public class Test {
	public static void main(String[] args) {
		ArrayList<Integer> l = new ArrayList<Integer>();
		l.add(1);
		l.add(4);
		l.removeAt(1);
		System.out.println(l.size());
	}
}
