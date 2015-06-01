package gool.test.lib;

import static org.junit.Assert.*;
import gool.generator.android.AndroidPlatform;
import gool.generator.common.Platform;
import gool.generator.cpp.CppPlatform;
import gool.generator.csharp.CSharpPlatform;
import gool.generator.java.JavaPlatform;
import gool.generator.objc.ObjcPlatform;
import gool.generator.python.PythonPlatform;
import gool.test.TestHelperJava;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

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

	(Platform) JavaPlatform.getInstance(),
	(Platform) CSharpPlatform.getInstance(),
	(Platform) CppPlatform.getInstance(),
	(Platform) PythonPlatform.getInstance()// ,
	// (Platform) AndroidPlatform.getInstance() ,
	// (Platform) ObjcPlatform.getInstance()

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

		public void compare(Platform platform, int test) throws Exception {
//			if (excludedPlatforms.contains(platform)) {
//				String errorMsg = "The following target platform(s) have been excluded for this test: ";
//				for (Platform p : excludedPlatforms)
//					if (testedPlatforms.contains(p))
//						errorMsg += p + " ";
//				Assert.fail(errorMsg
//						+ "\nThis test may contain some patterns that are not supported by GOOL at the moment for these target platforms. You may see the GOOL wiki for further documentation.");
//			}
			if (excludedPlatforms.contains(platform)){
				System.err.println("The following target platform(s) have been "
						+ "excluded for this test:" + platform.getName());
				return;
			}
			// This inserts a package which is mandatory for android
			// TODO Not the ideal place to put it also com.test should be in the
			// properties file
			if (platform instanceof AndroidPlatform) {
				this.input = "package com.test; " + input;
			}
			String result = compileAndRun(platform);
			// The following instruction is used to remove some logging data
			// at the beginning of the result string
			if (platform == ObjcPlatform.getInstance()
					&& result.indexOf("] ") != -1)
				result = result.substring(result.indexOf("] ") + 2);

			// Assert.assertEquals(String.format("The platform %s", platform),
			// expected, result);
			TestHelperJava.assertTestAPIFile(
					String.format("The platform %s", platform), expected,
					result, test);

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

	private static final String MAIN_CLASS_NAME = "TestTypeMapJavaToAny";

	private List<Platform> testNotImplementedOnPlatforms = new ArrayList<Platform>();

	private void excludePlatformForThisTest(Platform platform) {
		testNotImplementedOnPlatforms.add(platform);
	}

	@BeforeClass
	public static void init() {
	}

	@Test
	public void goolTypeMapJavaToCppTest01() throws Exception {
		String input = "import java.util.Map;"
				+ "import java.util.HashMap;"
				+ TestHelperJava
						.surroundWithClassMainFile(
								"/* Test creation of a map object */"
										+ "Map<Integer,Integer> m = new HashMap<Integer,Integer>();"
										
										+ "if(m != null){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}",
								MAIN_CLASS_NAME + "_Test01");
		String expected = "true";
		excludePlatformForThisTest((Platform) PythonPlatform.getInstance());
		compareResultsDifferentPlatforms(input, expected, 1);
	}
	
	@Test
	public void goolTypeMapJavaToCppTest02() throws Exception {
		String input = "import java.util.Map;"
				+ "import java.util.HashMap;"
				+ TestHelperJava
						.surroundWithClassMainFile(
								"/* Test isEmpty method translation */"
										+ "Map<Integer,Integer> m = new HashMap<Integer,Integer>();"
										
										+ "if(m.isEmpty()){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}",
								MAIN_CLASS_NAME + "_Test02");
		String expected = "true";

		compareResultsDifferentPlatforms(input, expected, 1);
	}
	
	@Test
	public void goolTypeMapJavaToCppTest03() throws Exception {
		String input = "import java.util.Map;"
				+ "import java.util.HashMap;"
				+ TestHelperJava
						.surroundWithClassMainFile(
								"/* Test size method translation */"
										+ "Map<Integer,Integer> m = new HashMap<Integer,Integer>();"
										
										+ "if(m.size() == 0){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}",
								MAIN_CLASS_NAME + "_Test03");
		String expected = "true";
		excludePlatformForThisTest((Platform) JavaPlatform.getInstance());
		compareResultsDifferentPlatforms(input, expected, 1);
	}
	
	@Test
	public void goolTypeMapJavaToCppTest04() throws Exception {
		String input = "import java.util.Map;"
				+ "import java.util.HashMap;"
				+ TestHelperJava
						.surroundWithClassMainFile(
								"/* Test put method translation */"
										+ "Map<Integer,Integer> m = new HashMap<Integer,Integer>();"
										+ "m.put(1,2);"
										+ "if(m.size() == 1){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}",
								MAIN_CLASS_NAME + "_Test04");
		String expected = "true";
		excludePlatformForThisTest((Platform) JavaPlatform.getInstance());
		compareResultsDifferentPlatforms(input, expected, 1);
	}
	
	@Test
	public void goolTypeMapJavaToCppTest05() throws Exception {
		String input = "import java.util.Map;"
				+ "import java.util.HashMap;"
				+ TestHelperJava
						.surroundWithClassMainFile(
								"/* Test containsKey method translation */"
										+ "Map<Integer,Integer> m = new HashMap<Integer,Integer>();"
										+ "m.put(1,2);"
										+ "if(m.containsKey(1)){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}",
								MAIN_CLASS_NAME + "_Test05");
		String expected = "true";

		compareResultsDifferentPlatforms(input, expected, 1);
	}
	
	@Test
	public void goolTypeMapJavaToCppTest06() throws Exception {
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
								MAIN_CLASS_NAME + "_Test06");
		String expected = "true";
		excludePlatformForThisTest((Platform) JavaPlatform.getInstance());
		compareResultsDifferentPlatforms(input, expected, 1);
	}
	
	@Test
	public void goolTypeMapJavaToCppTest07() throws Exception {
		String input = "import java.util.Map;"
				+ "import java.util.HashMap;"
				+ TestHelperJava
						.surroundWithClassMainFile(
								"/* Test get method translation */"
										+ "Map<Integer,Integer> m = new HashMap<Integer,Integer>();"
										+ "m.put(1,2);"
										+ "if(m.get(1) == 2){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}",
								MAIN_CLASS_NAME + "_Test07");
		String expected = "true";

		compareResultsDifferentPlatforms(input, expected, 1);
	}

	private void compareResultsDifferentPlatforms(String input,
			String expected, int test) throws Exception {
		compareResultsDifferentPlatforms(new GoolTestExecutor(input, expected,
				platforms, testNotImplementedOnPlatforms), test);
		this.testNotImplementedOnPlatforms = new ArrayList<Platform>();
	}

	private void compareResultsDifferentPlatforms(GoolTestExecutor executor,
			int test) throws Exception {
		for (Platform platform : platforms) {
			executor.compare(platform, test);
		}
	}

}
