import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;

public class Test {
	public static void main(String[] args) {
		File t = new File("test.txt");FileWriter fw = new FileWriter(t);fw.write("kllkl\nkj");fw.close();FileReader f = new FileReader(t);System.out.println(f.read());BufferedReader g = new BufferedReader(f); System.out.println(g.readLine());g.close();
	}
}
