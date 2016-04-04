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
import java.util.concurrent.Semaphore;

import org.junit.AfterClass;
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

	private static String MAIN_CLASS_NAME = "TestThreadTest";

	private List<Platform> testNotImplementedOnPlatforms = new ArrayList<Platform>();

	private void excludePlatformForThisTest(Platform platform) {
		testNotImplementedOnPlatforms.add(platform);
	}

	@BeforeClass
	public static void init() {
	}



	@Test
	public void goolLibraryThreadTest1() throws Exception {
		MAIN_CLASS_NAME = "TestThreadTest1";
		String input = "import java.lang.Thread;"
				+ "import java.lang.Runnable;"
				+ TestHelperJava
				.surroundWithClassMain(
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
		excludePlatformForThisTest((Platform) CppPlatform.getInstance());
		excludePlatformForThisTest((Platform) CSharpPlatform.getInstance());
		excludePlatformForThisTest((Platform) PythonPlatform.getInstance());
		excludePlatformForThisTest((Platform) ObjcPlatform.getInstance());

		compareResultsDifferentPlatforms(input, expected);
	}



	@Test
	public void goolLibraryThreadTest2() throws Exception {
		MAIN_CLASS_NAME = "TestThreadTest2";
		String static_var = "static int results = 0 ;"
							+ "static Semaphore semaphore = new Semaphore(1);";
		String input = "import java.lang.Thread;"
				+ "import java.lang.Runnable;"
				+ "import java.util.concurrent.Semaphore;"
				+ "/* création de 5 threads puis chacun affiche incrémente une variable */"
				+ TestHelperJava
				.surroundWithClassMainWithStaticVar(
								"Thread [] threads = new Thread[5];"
								+ "	for(int i = 0 ; i < 5 ; i++){"
								+ "		threads[i] = new Thread(new Runnable() {"
								+ "			@Override"
								+ "			public void run() {"
								+ "				for(int i = 0; i < 500 ; i++){"
								+ "					try {"
								+ "						" + MAIN_CLASS_NAME + ".semaphore.acquire();"
								+ "					} catch (Exception e) {	}"
								+ "					" + MAIN_CLASS_NAME + ".results++;"
								+ "					" + MAIN_CLASS_NAME + ".semaphore.release();"
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
								+ "	System.out.println(results);",
								MAIN_CLASS_NAME, static_var);
		String expected = "2500";
		System.out.println(input);
		// Matching of the GoolFile library class and of its method
		// work only for the Java target language at the moment,
		// so we exclude the other platforms for this test.
		excludePlatformForThisTest((Platform) CppPlatform.getInstance());
		excludePlatformForThisTest((Platform) CSharpPlatform.getInstance());
		excludePlatformForThisTest((Platform) PythonPlatform.getInstance());
		excludePlatformForThisTest((Platform) ObjcPlatform.getInstance());

		compareResultsDifferentPlatforms(input, expected);
	}


	@Test
	public void goolLibraryThreadTest3() throws Exception {
		MAIN_CLASS_NAME = "TestThreadTest3";
		String static_var = "static int results = 0 ;"
		+ "static Semaphore semaphore = new Semaphore(1);";		
		String input = "import java.lang.Thread;"
				+ "import java.lang.Runnable;"
				+ "import java.util.concurrent.Semaphore;"
				+ "/* création de 5 threads puis chacun affiche incrémenteune variable */"
				+ TestHelperJava
				.surroundWithClassMainWithStaticVar(
								"Thread [] threads = new Thread[10];"
								+ "	for(int i = 0 ; i < 10 ; i++){"
								+ "		threads[i] = new Thread(new Runnable() {"

								+ "			@Override"
								+ "			public void run() {"

								+ "				for(int i = 0; i < 100000 ; i++){"
								+ "					try {"
								+ "						" + MAIN_CLASS_NAME + ".semaphore.acquire();"
								+ "					} catch (Exception e) {	}"
								+ "					" + MAIN_CLASS_NAME + ".results++;"
								+ "					" + MAIN_CLASS_NAME + ".semaphore.release();"
								+ "					for(int k = 0; k < 100 ; k++){"
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
								+ "	System.out.println(results);",
								MAIN_CLASS_NAME, static_var);
		String expected = "1000000";

		// Matching of the GoolFile library class and of its method
		// work only for the Java target language at the moment,
		// so we exclude the other platforms for this test.
		excludePlatformForThisTest((Platform) CppPlatform.getInstance());
		excludePlatformForThisTest((Platform) CSharpPlatform.getInstance());
		excludePlatformForThisTest((Platform) PythonPlatform.getInstance());
		excludePlatformForThisTest((Platform) ObjcPlatform.getInstance());
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
