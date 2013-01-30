import java.io.File;

public class Test {
	public static void main(String[] args) {
		File f = new File("TestFolder");
		if(f.mkdir()) {System.out.println("Done");}
	}
}
