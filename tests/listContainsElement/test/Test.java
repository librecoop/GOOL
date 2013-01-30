import gool.imports.java.util.ArrayList;

public class Test {
	public static void main(String[] args) {
		ArrayList l = new ArrayList();
		l.add("hola");
		l.remove("hola");
		l.add("hola");
		if(l.contains("hola")) {
			System.out.println("Ok");
		}
	}
}
