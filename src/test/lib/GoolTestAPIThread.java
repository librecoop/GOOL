package gool.test.lib;

import gool.Settings;
import gool.generator.common.Platform;
import gool.generator.cpp.CppPlatform;
import gool.generator.csharp.CSharpPlatform;
import gool.generator.java.JavaPlatform;
import gool.generator.python.PythonPlatform;
import gool.generator.objc.ObjcPlatform;
import gool.test.TestHelperJava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
public class GoolTestAPIThread {

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
//			 (Platform) ObjcPlatform.getInstance()

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
			if (excludedPlatforms.contains(platform)) {
				String errorMsg = "The following target platform(s) have been excluded for this test: ";
				for (Platform p : excludedPlatforms)
					if (testedPlatforms.contains(p))
						errorMsg += p + " ";
				Assert.fail(errorMsg
						+ "\nThis test may contain some patterns that are not supported by GOOL at the moment for these target platforms. You may see the GOOL wiki for further documentation.");
			}

			String result = compileAndRun(platform);
			// The following instruction is used to remove some logging data
			// at the beginning of the result string
			if (platform == ObjcPlatform.getInstance()
					&& result.indexOf("] ") != -1)
				result = result.substring(result.indexOf("] ") + 2);

			//Assert.assertEquals(String.format("The platform %s", platform),
			//		expected, result);
			TestHelperJava.assertTestAPIFile(String.format("The platform %s", platform),
							expected, result, test);
			
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

	private static final String MAIN_CLASS_NAME = "Test";

	private List<Platform> testNotImplementedOnPlatforms = new ArrayList<Platform>();

	private void excludePlatformForThisTest(Platform platform) {
		testNotImplementedOnPlatforms.add(platform);
	}

	@BeforeClass
	public static void init() {
	}

	

	@Test
	public void goolLibraryThreadTest1() throws Exception {
		String input = "import java.lang.Thread;"
				+ "import java.lang.Runnable;"
				+ TestHelperJava
						.surroundWithClassMainThread(
								"/* création de 5 threads puis chacun affiche 1 */"
										+ "for(int i = 0 ; i < 5 ; i++){"
										+ "	new Thread(new Runnable() {"
												
										+ "		@Override"
										+ "		public void run() {"
										+ "			System.out.println(\"1\");"
										+ "		}"
										+ "	}).start();",
								MAIN_CLASS_NAME);
		String expected = "1" + "1" + "1" + "1" + "1";

		// Matching of the GoolFile library class and of its method
		// work only for the Java target language at the moment,
		// so we exclude the other platforms for this test.
		excludePlatformForThisTest((Platform) ObjcPlatform.getInstance());

		compareResultsDifferentPlatforms(input, expected, 1);
	}

	

	@Test
	public void goolLibraryThreadTest2() throws Exception {
		String input = "import java.lang.Thread;"
				+ "import java.lang.Runnable;"
				+ "import java.util.concurrent.Semaphore;"
				+ TestHelperJava
						.surroundWithClassMainThread(
								"/* création de 5 threads puis chacun affiche incrémenteune variable */"
								+ "static int results = 0 ;"
								+ "static Semaphore semaphore = new Semaphore(1);"
								+ "public static void main(String[] args) {"
								+ "Thread [] threads = new Thread[5];"
								+ "	for(int i = 0 ; i < 5 ; i++){"
								+ "		threads[i] = new Thread(new Runnable() {"

								+ "			@Override"
								+ "			public void run() {"

								+ "				for(int i = 0; i < 500 ; i++){"
								+ "					try {"
								+ "						semaphore.acquire();"
								+ "					} catch (Exception e) {	}"
								+ "					results++;"
								+ "					semaphore.release();"
								+ "				}"
								+ "			}"
								+ "		});"
								+ "	}"
								+ "	for(int i = 0 ; i < 5 ; i++){"
								+ "		threads[i].start();"
								+ "	}"
								+ "	for(int i = 0 ; i < 5 ; i++){"
								+ "		try {"
								+ "			threads[i].join();"
								+ "		} catch (Exception e) {	}"
								+ "	}"
								+ "	System.out.println(results);"
								+ "}",
								MAIN_CLASS_NAME);
		String expected = "2500";

		// Matching of the GoolFile library class and of its method
		// work only for the Java target language at the moment,
		// so we exclude the other platforms for this test.
		excludePlatformForThisTest((Platform) ObjcPlatform.getInstance());

		compareResultsDifferentPlatforms(input, expected, 2);
	}


	

	@Test
	public void goolLibraryThreadTest3() throws Exception {
		String input = "import java.lang.Thread;"
				+ "import java.lang.Runnable;"
				+ "import java.util.concurrent.Semaphore;"
				+ TestHelperJava
						.surroundWithClassMainThread(
								"/* création de 5 threads puis chacun affiche incrémenteune variable */"
								+ "static int results = 0 ;"
								+ "static Semaphore semaphore = new Semaphore(1);"
								+ "public static void main(String[] args) {"
								+ "Thread [] threads = new Thread[10];"
								+ "	for(int i = 0 ; i < 10 ; i++){"
								+ "		threads[i] = new Thread(new Runnable() {"

								+ "			@Override"
								+ "			public void run() {"

								+ "				for(int i = 0; i < 100000 ; i++){"
								+ "					try {"
								+ "						semaphore.acquire();"
								+ "					} catch (Exception e) {	}"
								+ "					results++;"
								+ "					semaphore.release();"
								+ "                 for(int k = 0; k < 100000 ; k++){"
								+ "					"
								+ "					}"
								+ "				}"
								+ "			}"
								+ "		});"
								+ "	}"
								+ "	for(int i = 0 ; i < 10 ; i++){"
								+ "		threads[i].start();"
								+ "	}"
								+ "	for(int i = 0 ; i < 10 ; i++){"
								+ "		try {"
								+ "			threads[i].join();"
								+ "		} catch (Exception e) {	}"
								+ "	}"
								+ "	System.out.println(results);"
								+ "}",
								MAIN_CLASS_NAME);
		String expected = "1000000";

		// Matching of the GoolFile library class and of its method
		// work only for the Java target language at the moment,
		// so we exclude the other platforms for this test.
		excludePlatformForThisTest((Platform) ObjcPlatform.getInstance());

		compareResultsDifferentPlatforms(input, expected, 3);
	}

	
	
	private void compareResultsDifferentPlatforms(String input, String expected, int test)
			throws Exception {
		compareResultsDifferentPlatforms(new GoolTestExecutor(input, expected,
				platforms, testNotImplementedOnPlatforms), test);
		this.testNotImplementedOnPlatforms = new ArrayList<Platform>();
	}

	private void compareResultsDifferentPlatforms(GoolTestExecutor executor, int test)
			throws Exception {
		for (Platform platform : platforms) {
			executor.compare(platform,test);
		}
	}
}
