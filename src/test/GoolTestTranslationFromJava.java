

import gool.GOOLCompiler;
import gool.Settings;
import gool.generator.common.Platform;
import gool.generator.cpp.CppPlatform;
import gool.generator.csharp.CSharpPlatform;
import gool.generator.java.JavaPlatform;
import gool.generator.python.PythonPlatform;
import gool.parser.java.JavaParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import gool.test.FileManager;
import logger.Log;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GoolTestTranslationFromJava {

	private static Collection<String> createdFileList = new ArrayList<String>();

	//For each platform added in the list, one should complete compare_files 
	private List<Platform> outPlatforms = Arrays.asList(
			(Platform) CppPlatform.getInstance(Settings.get("cpp_out_dir")),
			(Platform) PythonPlatform.getInstance(Settings.get("python_out_dir"))
			);

	private boolean runGoolCompilerOnOneJavaFile(String filename, Platform outPlatform){
		File javafiletocompile = new File(filename);
		GOOLCompiler gc=new GOOLCompiler();
		Map<String, String> files = new HashMap<String, String>();
		files.put(filename, gc.readFile(javafiletocompile));
		try{
			gc.runGOOLCompiler(new JavaParser(), outPlatform, files);
		}
		catch(Exception e){
			Log.e(e);
			return false;
		}
		finally{

		}
		return true;
	}

	private void simple_do_java_xx(String classname, String maincontent){
		String javain = Settings.get("java_in_dir");
		String s ="public class " + classname + " {	public static void main"
				+"(String[] args) {" + maincontent +"}}";
		String javafilename = javain + classname + ".java";
		createdFileList.add(javafilename);
		try {
			Log.d("----> " + javafilename);
			FileManager.write(javafilename, s);
		} catch(Exception e){
			Log.e(e);
			Assert.fail(e.getMessage());
		}
		for (Platform outPlatform : outPlatforms){
			if (!runGoolCompilerOnOneJavaFile(javafilename, outPlatform)){
				Assert.fail("Unable to compile " + javafilename);
			}
			compare_files(classname, outPlatform);
		}
	}

	private void compare_files(String classname, Platform outPlatform){
		//C++
		if (outPlatform instanceof CppPlatform){
			Log.d("----> CppPlatform : " + Settings.get("cpp_out_dir"));
			String reference = Settings.get("cpp_ref_dir") + classname + ".cpp";
			String output_basename = Settings.get("cpp_out_dir") + classname;
			String output = output_basename + ".cpp";
			createdFileList.add(output);
			createdFileList.add(output_basename + ".h");
			try{
				Assert.assertTrue(FileManager.compareFile(reference, output));
			} catch(Exception e){
				Log.e(e);
				Assert.fail(e.getMessage());
			}
		}
		//Python
		if (outPlatform instanceof PythonPlatform){
			String reference = Settings.get("python_ref_dir")+ classname +".py";
			String output = Settings.get("python_out_dir")+ classname + ".py";
			createdFileList.add(output);
			try{
				Assert.assertTrue(FileManager.compareFile(reference, output));
			} catch(Exception e){
				Log.e(e);
				Assert.fail(e.getMessage());
			}
		}
	}

	@BeforeClass
	public static void init() {
	}
	@Test
	public void SimpleForTranslationTest(){
		String classname = "SimpleFor";
		String maincontent = "int total = 0;"
				+ "for(int i = 0; i < 4; i++)"
				+ "{total++;}"
				+ "System.out.println(total);";
		simple_do_java_xx(classname, maincontent);
	}

	@Test
	public void SimpleWhileTranslationTest(){
		String classname = "SimpleWhile";
		String maincontent = "int i = 0;"
				+ "int total = 0;"
				+ "while(i < 4)"
				+ "{total++;i++;}"
				+ "System.out.println(total);";
		simple_do_java_xx(classname, maincontent);
	}

	@Test
	public void SimpleAddTranslationTest(){
		String classname = "SimpleAdd";
		String maincontent = "int n = 2+2;"
				+ "System.out.println(n);";
		simple_do_java_xx(classname, maincontent);
	}

	@Test
	public void SimpleSubTranslationTest(){
		String classname = "SimpleSub";
		String maincontent = "int n = 6-2;"
				+ "System.out.println(n);";
		simple_do_java_xx(classname, maincontent);
	}

	@Test
	public void SimpleDivTranslationTest(){
		String classname = "SimpleDiv";
		String maincontent = "int n = 8/2;"
				+ "System.out.println(n);";
		simple_do_java_xx(classname, maincontent);
	}

	@Test
	public void SimpleMultTranslationTest(){
		String classname = "SimpleMult";
		String maincontent = "int n = 2*2;"
				+ "System.out.println(n);";
		simple_do_java_xx(classname, maincontent);
	}

	@Test
	public void SimpleModTranslationTest(){
		String classname = "SimpleMod";
		String maincontent = "int n = 4%5;"
				+ "System.out.println(n);";
		simple_do_java_xx(classname, maincontent);
	}



	@AfterClass
	public static void clean(){
		for (String filename : createdFileList){
			File f = new File(filename);
			f.delete();
		}
	}
}
