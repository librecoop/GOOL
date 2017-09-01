package gool.test.lib;

import gool.Settings;
import gool.generator.common.Platform;
import gool.generator.cpp.CppPlatform;
import gool.generator.csharp.CSharpPlatform;
import gool.generator.java.JavaPlatform;
import gool.generator.python.PythonPlatform;
import gool.generator.objc.ObjcPlatform;
import gool.test.TestHelperJava;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
public class GoolTestAPIFile {

	/*
	 * At this day, the GOOL system supports 6 output languages that are
	 * symbolized by Platforms. You may comment/uncomment these platforms to
	 * enable/disable tests for the corresponding output language.
	 * 
	 * You may also add your own tests by creating a new method within this
	 * class preceded by a @Test annotation.
	 */
	private List<Platform> platforms = Arrays.asList(

			(Platform) JavaPlatform.getInstance(Settings.get("java_out_dir")),
			(Platform) CSharpPlatform.getInstance(Settings.get("csharp_out_dir")),
			(Platform) PythonPlatform.getInstance(Settings.get("python_out_dir")),
			(Platform) CppPlatform.getInstance(Settings.get("cpp_out_dir"))//,
			//(Platform) ObjcPlatform.getInstance(Settings.get("objc_out_dir"))

			);

	private static class GoolTestExecutor {
		private static final String CLEAN_UP_REGEX = "Note:.*?[\r\n]|(\\w+>\\s)|[\\r\\n]+";
		private String input;
		private String expected;
		private List<Platform> testedPlatforms;
		private List<Platform> excludedPlatforms;

		public GoolTestExecutor(String input, String expected,
				List<Platform> testedPlatforms, List<Platform> excludedPlatforms) {
			this.input = input;
			this.expected = expected;
			this.testedPlatforms = testedPlatforms;
			this.excludedPlatforms = excludedPlatforms;
		}

		public void compare(Platform platform) throws Exception {
			if (excludedPlatforms.contains(platform)){
				System.err.println("The following target platform(s) have been "
						+ "excluded for this test:" + platform.getName());
				return;
			}

			String result = compileAndRun(platform);
			// The following instruction is used to remove some logging data
			// at the beginning of the result string
			if (platform == ObjcPlatform.getInstance()
					&& result.indexOf("] ") != -1)
				result = result.substring(result.indexOf("] ") + 2);

			Assert.assertEquals(String.format("The platform %s", platform),
					expected, result);
			
		}

		protected String compileAndRun(Platform platform) throws Exception {
			String cleanOutput = cleanOutput(TestHelperJava.generateCompileRun(
					platform, input, MAIN_CLASS_NAME));
			return cleanOutput;
		}

		private static String cleanOutput(String result) {
			return result.replaceAll(CLEAN_UP_REGEX, "").trim();
		}
	}

	private static String MAIN_CLASS_NAME = "TestFileTest";

	private List<Platform> testNotImplementedOnPlatforms = new ArrayList<Platform>();

	private void excludePlatformForThisTest(Platform platform) {
		testNotImplementedOnPlatforms.add(platform);
	}

	@BeforeClass
	public static void init() {
	}

	

	@Test
	public void goolLibraryFileTest1() throws Exception {
		MAIN_CLASS_NAME = "TestFileTest1";
		String input = "import java.io.File;"
				+ TestHelperJava
						.surroundWithClassMainFile(
								"/* création puis suppression d'un fichier qui n'existait pas */"
										+ "try{"
										+ "File f = new File(\"../testGool.txt\");"
										+

										"if(f.exists()){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}"
										+

										"if(f.createNewFile()){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\"); }"
										+

										"if(f.exists()){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}"
										+

										"if(f.delete()){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\"); }"
										+

										"if(f.exists()){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}"
										+ "}catch(Exception e){" + "}",
								MAIN_CLASS_NAME);
		String expected = "false" + "true" + "true" + "true" + "false";

		// Matching of the GoolFile library class and of its method
		// work only for the Java target language at the moment,
		// so we exclude the other platforms for this test.
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void goolLibraryFileTest2() throws Exception {
		MAIN_CLASS_NAME = "TestFileTest2";
		String input = "import java.io.File;"
				+ "import java.io.BufferedReader;"
				+ "import java.io.FileReader;"
				+ "import java.io.BufferedWriter;"
				+ "import java.io.FileWriter;"
				+

				TestHelperJava
						.surroundWithClassMainFile(
								"/* Creation d'un fichier, écriture, lecture, puis suppression du fichier. */"
										+ "try{"
										+ "File f = new File(\"../testGool.txt\");"
										+ "f.createNewFile();"
										+

										"FileWriter fw=new FileWriter(f);"
										+ "BufferedWriter bw=new BufferedWriter(fw);"
										+ "bw.write('a'); bw.write('b'); bw.close();"
										+

										"FileReader fr=new FileReader(f);"
										+ "BufferedReader br=new BufferedReader(fr);"
										+ "char c1=(char)br.read(), c2=(char)br.read(); br.close();"
										+ "System.out.println(c1+\"\"+c2);" +

										"f.delete();" + "}catch(Exception e){"
										+ "}", MAIN_CLASS_NAME);
		String expected = "ab";

		// Matching of the io GOOL library with classes and methods
		// of the output language work only for the Java target at the moment,
		// so we exclude the other platforms for this test.

		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void goolLibraryFileTest3() throws Exception {
		MAIN_CLASS_NAME = "TestFileTest3";
		String input = "import java.io.File;"
				+ "import java.io.BufferedReader;"
				+ "import java.io.FileReader;"
				+ "import java.io.BufferedWriter;"
				+ "import java.io.FileWriter;"
				+

				TestHelperJava.surroundWithClassMainFile(
						"/* Creation d'un fichier, écriture, lecture, puis suppression du fichier. */"
								+ "try{"
								+ "File f = new File(\"../testGool.txt\");"
								+ "f.createNewFile();" +

								"FileWriter fw=new FileWriter(f);"
								+ "BufferedWriter bw=new BufferedWriter(fw);"
								+ "String s=\"hello world\\n42\\n\";"
								+ "bw.write(s,0,s.length()); bw.close();" +

								"FileReader fr=new FileReader(f);"
								+ "BufferedReader br=new BufferedReader(fr);"
								+ "String line=br.readLine();"
								+ "while(line!=null){"
								+ "System.out.println(line);"
								+ "line=br.readLine();" + "}" + "br.close();"
								+ "f.delete();" + "}catch(Exception e){" + "}",
						MAIN_CLASS_NAME);
		String expected = "hello world42";

		// Matching of the io GOOL library with classes and methods
		// of the output language work only for the Java target at the moment,
		// so we exclude the other platforms for this test.
		compareResultsDifferentPlatforms(input, expected);
	}
	
	private void compareResultsDifferentPlatforms(String input, String expected)
			throws Exception {
		compareResultsDifferentPlatforms(new GoolTestExecutor(input, expected,
				platforms, testNotImplementedOnPlatforms));
		this.testNotImplementedOnPlatforms = new ArrayList<Platform>();
	}

	private void compareResultsDifferentPlatforms(GoolTestExecutor executor)
			throws Exception {
		for (Platform platform : platforms) {
			executor.compare(platform);
		}
	}
	
	@AfterClass
	public static void clean(){
		File dir = new File(Settings.get("java_out_dir"));
		cleanDir(dir);
		dir = new File(Settings.get("cpp_out_dir"));
		cleanDir(dir);
		dir = new File(Settings.get("csharp_out_dir"));
		cleanDir(dir);
		dir = new File(Settings.get("python_out_dir"));
		cleanDir(dir);
		dir = new File(Settings.get("objc_out_dir"));
		cleanDir(dir);
	}

	private static void cleanDir(File dir){
		if (!dir.exists())
			return;
		for (File f : dir.listFiles()){
			if (f.isDirectory()){
				cleanDir(f);
			}
			else{
				f.delete();
			}
		}
		dir.delete();
	}
}
