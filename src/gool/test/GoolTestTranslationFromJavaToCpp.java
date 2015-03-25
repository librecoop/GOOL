package gool.test;

import gool.GOOLCompiler;
import gool.Settings;
import gool.generator.cpp.CppPlatform;
import gool.parser.java.JavaParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;

import gool.test.FileManager;
import logger.Log;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GoolTestTranslationFromJavaToCpp {

	@Test
	public void SimpleForTranslationTest(){
		try {
			String javain = Settings.get("java_in_dir");

			String s ="public class SimpleFor {public static void main(String[] args) {int total = 0;for(int i = 0; i < 4; i++){total++;}System.out.println(total);}}";			
			FileManager.write(javain+"SimpleFor.java", s);

			runGoolCompiler();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		String reference = Settings.get("cpp_ref_dir")+"SimpleFor.cpp";
		String output = Settings.get("cpp_out_dir")+"SimpleFor.cpp";
		try{
			Assert.assertTrue(FileManager.compareFile(reference, output));
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void SimpleWhileTranslationTest(){
		try {
			String javain = Settings.get("java_in_dir");

			String s ="public class SimpleWhile {public static void main(String[] args) {int i = 0; int total = 0;	while(i < 4){total++;i++;}System.out.println(total);}}";			
			FileManager.write(javain+"SimpleWhile.java", s);

			runGoolCompiler();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		String reference = Settings.get("cpp_ref_dir")+"SimpleWhile.cpp";
		String output = Settings.get("cpp_out_dir")+"SimpleWhile.cpp";
		try{
			Assert.assertTrue(FileManager.compareFile(reference, output));
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void SimpleAddTranslationTest(){
		try {
			String javain = Settings.get("java_in_dir");

			String s ="public class SimpleAdd {	public static void main(String[] args) {		int n = 2+2;		System.out.println(n);	}}";			
			FileManager.write(javain+"SimpleAdd.java", s);

			runGoolCompiler();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		String reference = Settings.get("cpp_ref_dir")+"SimpleAdd.cpp";
		String output = Settings.get("cpp_out_dir")+"SimpleAdd.cpp";
		try{
			Assert.assertTrue(FileManager.compareFile(reference, output));
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void SimpleSubTranslationTest(){
		try {
			String javain = Settings.get("java_in_dir");

			String s ="public class SimpleSub {	public static void main(String[] args) {		int n = 6-2;		System.out.println(n);	}}";			
			FileManager.write(javain+"SimpleSub.java", s);

			runGoolCompiler();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		String reference = Settings.get("cpp_ref_dir")+"SimpleSub.cpp";
		String output = Settings.get("cpp_out_dir")+"SimpleSub.cpp";
		try{
			Assert.assertTrue(FileManager.compareFile(reference, output));
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void SimpleDivTranslationTest(){
		try {
			String javain = Settings.get("java_in_dir");

			String s ="public class SimpleDiv {	public static void main(String[] args) {	int n = 8/2;		System.out.println(n);	}}";			
			FileManager.write(javain+"SimpleDiv.java", s);

			runGoolCompiler();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		String reference = Settings.get("cpp_ref_dir")+"SimpleDiv.cpp";
		String output = Settings.get("cpp_out_dir")+"SimpleDiv.cpp";
		try{
			Assert.assertTrue(FileManager.compareFile(reference, output));
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void SimpleMultTranslationTest(){
		try {
			String javain = Settings.get("java_in_dir");

			String s ="public class SimpleMult {	public static void main(String[] args) {	int n = 2*2;		System.out.println(n);	}}";			
			FileManager.write(javain+"SimpleMult.java", s);

			runGoolCompiler();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		String reference = Settings.get("cpp_ref_dir")+"SimpleMult.cpp";
		String output = Settings.get("cpp_out_dir")+"SimpleMult.cpp";
		try{
			Assert.assertTrue(FileManager.compareFile(reference, output));
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void SimpleModTranslationTest(){
		try {
			String javain = Settings.get("java_in_dir");

			String s ="public class SimpleMod {	public static void main(String[] args) {int n = 4%5;	System.out.println(n);	}}";			
			FileManager.write(javain+"SimpleMod.java", s);

			runGoolCompiler();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		String reference = Settings.get("cpp_ref_dir")+"SimpleMod.cpp";
		String output = Settings.get("cpp_out_dir")+"SimpleMod.cpp";
		try{
			Assert.assertTrue(FileManager.compareFile(reference, output));
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void runGoolCompiler(){
		try {
			String javain = Settings.get("java_in_dir");
			File folder = new File(javain);


			Collection<File> files = getFilesInFolder(folder, "java");
			ArrayList<String> extToNCopy = new ArrayList<String>();

			BufferedReader g = null;
			try {
				File t = new File(javain + File.separator
						+ ".goolIgnore");
				FileReader f = new FileReader(t);
				g = new BufferedReader(f);
				String ligne;
				while ((ligne = g.readLine()) != null)
					extToNCopy.add(ligne);
			} catch (Exception e) {
				Log.e(e);
			} finally{
				g.close();
			}

			Collection<File> filesNonChange = getFilesInFolderNonExe(folder,
					extToNCopy);

			GOOLCompiler gc=new GOOLCompiler();

			// JAVA input -> CPP output
			gc.runGOOLCompiler(new JavaParser(), CppPlatform.getInstance(filesNonChange), files);
			// JAVA input -> PYTHON output
			//gc.runGOOLCompiler(new JavaParser(), PythonPlatform.getInstance(filesNonChange), files);

			for(File f : files){
				f.delete();
			}
		} catch (Exception e) {
			Log.e(e);
		}
	}

	public static Collection<File> getFilesInFolder(File folder, String ext) {
		Collection<File> files = new ArrayList<File>();
		for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				files.addAll(getFilesInFolder(f, ext));
			} else if (f.getName().endsWith(ext)) {
				files.add(f);
			}
		}
		return files;
	}

	private static Collection<File> getFilesInFolderNonExe(File folder,
			ArrayList<String> ext) {

		Collection<File> files = new ArrayList<File>();

		for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				files.addAll(getFilesInFolderNonExe(f, ext));
			} else {
				boolean trouve = false;
				for (String s : ext) {
					if (f.getName().endsWith(s))
						trouve = true;
				}
				if (!trouve)
					files.add(f);
				trouve = false;

			}
		}
		return files;
	}
	
	@AfterClass
	public static void clean(){
		String foldername = Settings.get("cpp_out_dir");
		File folder = new File(foldername);
		Collection<File> cpp_files = getFilesInFolder(folder, "cpp");
		Collection<File> h_files = getFilesInFolder(folder, "h");
		
		for(File f : cpp_files){
			f.delete();
		}
		
		for(File f : h_files){
			f.delete();
		}
	}
	

}
