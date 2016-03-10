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
import logger.Log;

public class FileManager {

	public static void write(String fileName, String s) throws IOException{
		File f = new File(fileName);
		if (!f.exists()) {
			try{
				f.getParentFile().mkdirs();
				if (!f.createNewFile())
					Log.e("FileManager ----> " + fileName + "Not created");
			}catch(IOException e){
				throw new IOException(e.getMessage() + " with file " + fileName);
			}catch (Exception e){
				throw e;
			}
		}
		BufferedWriter out = new BufferedWriter(new FileWriter(f));
		try {
			out.write(s);
		}catch(IOException e){
			throw new IOException(e.getMessage() + " with file " + fileName);
		}finally{
			out.close();
		}
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
			Log.e(" Goldfile " + goldfile + " is different than test file " + testfile + " at :" + delta.toString());
			return false;
		}
		return true;
	}
}
