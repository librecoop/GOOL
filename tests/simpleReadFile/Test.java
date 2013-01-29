import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

public class Test {
	public static void main(String[] args) {
		File t = new File("goolHelper.py");
		FileReader f = new FileReader(t);
		System.out.println(f.read());
		BufferedReader g = new BufferedReader(f);
		System.out.println(g.readLine());
	}
}
