/*
 * march 2015
 * File written by M2 FSI students
 */

package gool.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import difflib.*;

public class FileManager {

	public static void write(String fileName, String s) throws IOException{
//		File f = new File(fileName);
//		if (!f.exists()) {
//			f.createNewFile();
//		}
//		BufferedWriter out = new BufferedWriter(new FileWriter(f));
//		try {
//			out.write(s);
//		}catch(IOException e){
//			throw new IOException(e.getMessage() + " with file " + fileName);
//		}finally{
//			out.close();
//		}
		throw new IOException("Mon exception with file " + fileName);
	}

	private static List<String> fileToLines(String filename) throws IOException{
		//System.out.println("\n" + filename + "\n");
		List<String> lines = new LinkedList<String>();
		String line = "";
		BufferedReader in = new BufferedReader(new FileReader(filename));
		try {
			while ((line = in.readLine()) != null) {
				line = line.trim(); // remove leading and trailing whitespace
				if (!line.isEmpty()){
					//System.out.println(line);
					lines.add(line);
				}
			}
		} catch (IOException e) {
			throw new IOException(e.getMessage() + " with file " + filename);
		}finally{
			in.close();
		}
		return lines;
	}

	public static boolean compareFile(String goldfile, String testfile)
			throws Exception {
		List<String> gold, test;
		try{
			gold = fileToLines(goldfile);
			test  = fileToLines(testfile);
		}catch(IOException e){
			throw new IOException(e.getMessage() + " with goldfile " + goldfile + " and test file " + testfile);
		}
		// Compute diff. Get the Patch object. Patch is the container for computed deltas.
		Patch patch = DiffUtils.diff(gold, test);
		for (Delta delta: patch.getDeltas()) {
			System.out.println(delta);
			return false;
		}
		return true;
	}
}
