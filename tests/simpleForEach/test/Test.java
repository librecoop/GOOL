import gool.imports.java.util.ArrayList;

public class Test {
	public static void main(String[] args) {
		Integer total = 0;
		ArrayList<Integer> l = new ArrayList<Integer>();
		l.add(-2); l.add(-1);l.add(0); l.add(1); l.add(2);l.add(4);
		for(Integer i : l){total = total + i;}
		System.out.println(total);
	}
}
