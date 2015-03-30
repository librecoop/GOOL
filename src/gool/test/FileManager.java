package gool.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;

public class FileManager {
	/**
	 *
	 * @param fileName
	 * @param s
	 * @return
	 */
	public static boolean write(String fileName, String s) {
		File f = new File(fileName);
		// Le try catch est uniquement pour eviter une erreur d'I/O, l'existence
		// du fichier etant (quasi)assuree
		try {
			if (!f.exists()) {
				f.createNewFile();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(f));
			out.write(s);
			out.close();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	/**
	 *
	 * @param fileName
	 * @return
	 */
	public static String read(String fileName) {
		File f = new File(fileName);
		StringBuilder s = new StringBuilder();
		// Le try catch est uniquement pour eviter une erreur d'I/O, l'existence
		// du fichier etant (quasi)assuree
		try {
			if (!f.exists()) {
				f.createNewFile();
			}
			BufferedReader in = new BufferedReader(new FileReader(f));
			String line = in.readLine();
			if (line != null)
				s.append(line);
			line = in.readLine();
			while (line != null) {
				s.append("\n" + line);
				line = in.readLine();
			}
			in.close();
			return s.toString();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "";
		}
	}

	public static void Delete(String filename) {
		try {
			File file = new File(filename);
			if (file.delete()) {
				System.out.println(file.getName() + " is deleted!");
			} else {
				System.out.println("Delete operation is failed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean compareFile(String file1, String file2)
			throws Exception {
		File f1 = new File(file1); // OUTFILE
		File f2 = new File(file2); // INPUT
		FileReader fR1 = new FileReader(f1);
		FileReader fR2 = new FileReader(f2);
		BufferedReader reader1 = new BufferedReader(fR1);
		BufferedReader reader2 = new BufferedReader(fR2);
		String line1 = null;
		String line2 = null;
		boolean res = true;
		while (((line1 = reader1.readLine()) != null)
				&& ((line2 = reader2.readLine()) != null)) {
			if (!line1.equalsIgnoreCase(line2)) {
				res = false;
				break;
			}
		}
		reader1.close();
		reader2.close();
		return res;
	}
}