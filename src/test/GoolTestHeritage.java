
package gool.test;

import gool.Settings;
import gool.generator.common.Platform;
import gool.generator.cpp.CppPlatform;
import gool.generator.csharp.CSharpPlatform;
import gool.generator.java.JavaPlatform;
import gool.generator.python.PythonPlatform;
import gool.generator.objc.ObjcPlatform;
import gool.parser.java.JavaParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GoolTestHeritage {

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

	private static final String MAIN_CLASS_NAME = "testHeritage";

	private List<Platform> testNotImplementedOnPlatforms = new ArrayList<Platform>();

	private void excludePlatformForThisTest(Platform platform) {
		testNotImplementedOnPlatforms.add(platform);
	}

	@BeforeClass
	public static void init() {
	}

	@Test
	public void heritageTest() throws Exception{
		String input = TestHelperJava.surroundWithExtendedClass(
				"public void name() {System.out.println(\"Daughter\");}"
				+ "public static void main(String[] args){"
				+ MAIN_CLASS_NAME + " daughter = new " + MAIN_CLASS_NAME + "();"
				+ "daughter.name();"
				+ MAIN_CLASS_NAME + "Mother mother = new " + MAIN_CLASS_NAME + "Mother();"
				+ "mother.name();"
				+ "}"
				, MAIN_CLASS_NAME, MAIN_CLASS_NAME + "Mother", "");
		input += "\n" + TestHelperJava.surroundWithClass(
				"public void name()  {System.out.println(\"Mother\");}"
				,MAIN_CLASS_NAME + "Mother", "");
		
		String expected="Daughter" + "Mother";
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

}


