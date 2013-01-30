import gool.imports.java.util.ArrayList;

public class Test {
	public static void main(String[] args) {
		ArrayList l = new ArrayList();
		l.add("hola");
		l.remove("hola");
		if(l.isEmpty()) {
			System.out.println("Ok");
		}
	}
}
