package gool.test.lib;

import static org.junit.Assert.*;

import gool.Settings;
import gool.generator.common.Platform;
import gool.generator.cpp.CppPlatform;
import gool.generator.csharp.CSharpPlatform;
import gool.generator.java.JavaPlatform;
import gool.generator.objc.ObjcPlatform;
import gool.generator.python.PythonPlatform;
import gool.test.TestHelperJava;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class GoolTestTypeMapJavaToAny {
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

	private static String MAIN_CLASS_NAME = "TestTypeMapJavaToAny";

	private List<Platform> testNotImplementedOnPlatforms = new ArrayList<Platform>();

	private void excludePlatformForThisTest(Platform platform) {
		testNotImplementedOnPlatforms.add(platform);
	}

	@BeforeClass
	public static void init() {
	}

	@Test
	public void goolTypeMapJavaToCppTest01() throws Exception {
		MAIN_CLASS_NAME = "TestTypeMapJavaToCpp01";
		String input = "import java.util.Map;"
				+ "import java.util.HashMap;"
				+ TestHelperJava
						.surroundWithClassMainFile(
								"/* Test creation of a map object */"
										+ "Map<Integer,Integer> m = new HashMap<Integer,Integer>();"
										
										+ "if(m != null){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}",
								MAIN_CLASS_NAME);
		String expected = "true";
		excludePlatformForThisTest((Platform) PythonPlatform.getInstance());
		compareResultsDifferentPlatforms(input, expected);
	}
	
	@Test
	public void goolTypeMapJavaToCppTest02() throws Exception {
		MAIN_CLASS_NAME = "TestTypeMapJavaToCpp02";
		String input = "import java.util.Map;"
				+ "import java.util.HashMap;"
				+ TestHelperJava
						.surroundWithClassMainFile(
								"/* Test isEmpty method translation */"
										+ "Map<Integer,Integer> m = new HashMap<Integer,Integer>();"
										+ "if(m.isEmpty()){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}",
								MAIN_CLASS_NAME);
		String expected = "true";

		compareResultsDifferentPlatforms(input, expected);
	}
	
	@Test
	public void goolTypeMapJavaToCppTest03() throws Exception {
		MAIN_CLASS_NAME = "TestTypeMapJavaToCpp03";
		String input = "import java.util.Map;"
				+ "import java.util.HashMap;"
				+ TestHelperJava
						.surroundWithClassMainFile(
								"/* Test size method translation */"
										+ "Map<Integer,Integer> m = new HashMap<Integer,Integer>();"
										
										+ "if(m.size() == 0){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}",
								MAIN_CLASS_NAME);
		String expected = "true";
		//excludePlatformForThisTest((Platform) JavaPlatform.getInstance());
		compareResultsDifferentPlatforms(input, expected);
	}
	
	@Test
	public void goolTypeMapJavaToCppTest04() throws Exception {
		MAIN_CLASS_NAME = "TestTypeMapJavaToCpp04";
		String input = "import java.util.Map;"
				+ "import java.util.HashMap;"
				+ TestHelperJava
						.surroundWithClassMainFile(
								"/* Test put method translation */"
										+ "Map<Integer,Integer> m = new HashMap<Integer,Integer>();"
										+ "m.put(1,2);"
										+ "if(m.size() == 1){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}",
								MAIN_CLASS_NAME);
		String expected = "true";
		//excludePlatformForThisTest((Platform) JavaPlatform.getInstance());
		compareResultsDifferentPlatforms(input, expected);
	}
	
	@Test
	public void goolTypeMapJavaToCppTest05() throws Exception {
		MAIN_CLASS_NAME = "TestTypeMapJavaToCpp05";
		String input = "import java.util.Map;"
				+ "import java.util.HashMap;"
				+ TestHelperJava
						.surroundWithClassMainFile(
								"/* Test containsKey method translation */"
										+ "Map<Integer,Integer> m = new HashMap<Integer,Integer>();"
										+ "m.put(1,2);"
										+ "if(m.containsKey(1)){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}",
								MAIN_CLASS_NAME);
		String expected = "true";

		compareResultsDifferentPlatforms(input, expected);
	}
	
	@Test
	public void goolTypeMapJavaToCppTest06() throws Exception {
		MAIN_CLASS_NAME = "TestTypeMapJavaToCpp06";
		String input = "import java.util.Map;"
				+ "import java.util.HashMap;"
				+ TestHelperJava
						.surroundWithClassMainFile(
								"/* Test remove method translation */"
										+ "Map<Integer,Integer> m = new HashMap<Integer,Integer>();"
										+ "m.put(1,2);"
										+ "m.remove(1);"
										+ "if(m.size() == 0){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}",
								MAIN_CLASS_NAME);
		String expected = "true";
		//excludePlatformForThisTest((Platform) JavaPlatform.getInstance());
		compareResultsDifferentPlatforms(input, expected);
	}
	
	@Test
	public void goolTypeMapJavaToCppTest07() throws Exception {
		MAIN_CLASS_NAME = "TestTypeMapJavaToCpp07";
		String input = "import java.util.Map;"
				+ "import java.util.HashMap;"
				+ TestHelperJava
						.surroundWithClassMainFile(
								"/* Test get method translation */"
										+ "Map<Integer,Integer> m = new HashMap<Integer,Integer>();"
										+ "m.put(1,2);"
										+ "if(m.get(1) == 2){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}",
								MAIN_CLASS_NAME);
		String expected = "true";

		compareResultsDifferentPlatforms(input, expected);
	}

	private void compareResultsDifferentPlatforms(String input,
			String expected) throws Exception {
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
