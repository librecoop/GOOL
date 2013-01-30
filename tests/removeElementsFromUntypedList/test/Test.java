import gool.imports.java.util.ArrayList;

public class Test {
	public static void main(String[] args) {
		ArrayList l = new ArrayList();
		l.add("");
		l.add("hola");
		l.remove("hola");
		System.out.println(l.size());
	}
}
