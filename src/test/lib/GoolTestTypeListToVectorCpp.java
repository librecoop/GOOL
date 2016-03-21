package gool.test.lib;


import gool.Settings;
import gool.generator.common.Platform;
import gool.generator.cpp.CppPlatform;
import gool.generator.csharp.CSharpPlatform;
import gool.generator.java.JavaPlatform;
import gool.generator.objc.ObjcPlatform;
import gool.generator.python.PythonPlatform;
import gool.test.TestHelperJava;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class GoolTestTypeListToVectorCpp {
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

	private static String MAIN_CLASS_NAME = "Test";

	private List<Platform> testNotImplementedOnPlatforms = new ArrayList<Platform>();

	private void excludePlatformForThisTest(Platform platform) {
		testNotImplementedOnPlatforms.add(platform);
	}

	@BeforeClass
	public static void init() {
	}

	/*
	 * Creates a list to be translated into a vector (CPP only)
	 * Tested methods:
	 * 		add(E e), contains(object o), isEmpty()
	 * contains is not implemented yet
	 */
	@Test
	public void goolLibraryJavaListToCppVectorTestAddContains() throws Exception {
		MAIN_CLASS_NAME = "TestTypeListToVectorCpp_TestAddContains";
		String input = "import java.util.List;"
				+ "import java.util.ArrayList;"
				+ TestHelperJava
				.surroundWithClassMainFile(
						"/* creation of a list -- add + contains */"
								+ "try{"
								+ "List<String> list = new ArrayList<String>();"
								+

										"if(list.isEmpty()){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}"
										+ "list.add(\"toto\");"
										+

										"if(list.contains(\"toto\")){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}"
										+ "}catch(Exception e){System.out.println(e.toString())}",
										MAIN_CLASS_NAME);
		String expected = "true" + "true";
		compareResultsDifferentPlatforms(input, expected);
	}

	/*
	 * Creates a list to be translated into a vector (CPP only)
	 * Tested methods:
	 * 		add(E e), remove(object o), isEmpty()
	 */
	@Test
	public void goolLibraryJavaListToCppVectorTestRemove() throws Exception {
		MAIN_CLASS_NAME = "TestTypeListToVectorCpp_TestRemove";
		String input = "import java.util.List;"
				+ "import java.util.ArrayList;"
				+ TestHelperJava
				.surroundWithClassMainFile(
						"/* creation of a list -- add + remove */"
								+ "try{"
								+ "List<String> list = new ArrayList<String>();"
								+

										"if(list.isEmpty()){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}"
										+ "list.add(\"toto\");"
										+ "list.remove(\"toto\");"
										+

										"if(list.isEmpty()){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}"
										+ "}catch(Exception e){System.out.println(e.toString())}",
										MAIN_CLASS_NAME);
		String expected = "true" + "true";
		compareResultsDifferentPlatforms(input, expected);
	}

	/*
	 * Creates a list to be translated into a vector (CPP only)
	 * Tested methods:
	 * 		add(E e), add(int Index, E e), get(int Index)
	 */
	@Test
	public void goolLibraryJavaListToCppVectorTestAddGet() throws Exception {
		MAIN_CLASS_NAME = "TestTypeListToVectorCpp_TestAddGet";
		String input = "import java.util.List;"
				+ "import java.util.ArrayList;"
				+ TestHelperJava
				.surroundWithClassMainFile(
						"/* creation of a list -- add + insert + get */"
								+ "try{"
								+ "List<String> list = new ArrayList<String>();"
								+

										"if(list.isEmpty()){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}"
										+ "list.add(\"toto\");"
										+ "list.add(0, \"tata\");"
										+

										"if(\"tata\" == list.get(0)){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\"); }"
										+ "}catch(Exception e){System.out.println(e.toString())}",
										MAIN_CLASS_NAME);
		String expected = "true" + "true";
		compareResultsDifferentPlatforms(input, expected);
	}

	/*
	 * Creates a list to be translated into a vector (CPP only)
	 * Tested methods:
	 * 		add(E e), size(), clear(), isEmpty()
	 */
	@Test
	public void goolLibraryJavaListToCppVectorTestSizeClear() throws Exception {
		MAIN_CLASS_NAME = "TestTypeListToVectorCpp_TestSizeClear";
		String input = "import java.util.List;"
				+ "import java.util.ArrayList;"
				+ TestHelperJava
				.surroundWithClassMainFile(
						"/* creation of a list -- size + clear */"
								+ "try{"
								+ "List<String> list = new ArrayList<String>();"
								+

										"if(list.isEmpty()){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\");}"
										+ "list.add(\"toto\");"
										+ "list.add(\"tata\");"
										+

										"if(2 == list.size()){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\"); }"
										+ "list.clear();"
										+

										"if(list.isEmpty()){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\"); }"
										+ "}catch(Exception e){System.out.println(e.toString())}",
										MAIN_CLASS_NAME);
		String expected = "true" + "true" + "true";

		compareResultsDifferentPlatforms(input, expected);
	}

	/*
	 * Creates a list to be translated into a vector (CPP only)
	 * Tested methods:
	 * 		add(E e), equals(Object o)
	 */
	@Test
	public void goolLibraryJavaListToCppVectorTestEquals() throws Exception {
		MAIN_CLASS_NAME = "TestTypeListToVectorCpp_TestEquals";
		String input = "import java.util.List;"
				+ "import java.util.ArrayList;"
				+ TestHelperJava
				.surroundWithClassMainFile(
						"/* creation of a list -- equals + indexOf */"
								+ "try{"
								+ "List<String> list1 = new ArrayList<String>();"
								+ "List<String> list2 = new ArrayList<String>();"
								+

										"list1.add(\"toto\");"
										+ "list1.add(\"tata\");"
										+ "list2.add(\"toto\");"
										+ "list2.add(\"tata\");"
										+

										"if(list1.equals(list2)){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\"); }"
										+ "list2.add(\"titi\");"
										+

										"if(list1.equals(list2)){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\"); }"
										+ "}catch(Exception e){System.out.println(e.toString())}",
										MAIN_CLASS_NAME);
		String expected = "true" + "false";
		compareResultsDifferentPlatforms(input, expected);
	}

	/*
	 * Creates a list to be translated into a vector (CPP only)
	 * Tested methods:
	 * 		add(E e), indexOf(Object o)
	 */
	@Test
	public void goolLibraryJavaListToCppVectorTestIndexOf() throws Exception {
		MAIN_CLASS_NAME = "TestTypeListToVectorCpp_TestIndexOf";
		String input = "import java.util.List;"
				+ "import java.util.ArrayList;"
				+ TestHelperJava
				.surroundWithClassMainFile(
						"/* creation of a list -- equals + indexOf */"
								+ "try{"
								+ "List<String> list1 = new ArrayList<String>();"
								+ "List<String> list2 = new ArrayList<String>();"
								+ 

										"list1.add(\"toto\");"
										+ "list1.add(\"tata\");"
										+ "list1.add(\"titi\");"
										+

										"if(1 == list1.indexOf(\"tata\")){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\"); }"
										+ "}catch(Exception e){System.out.println(e.toString())}",
										MAIN_CLASS_NAME);
		String expected = "true";
		compareResultsDifferentPlatforms(input, expected);
	}

	/*
	 * Creates a list to be translated into a vector (CPP only)
	 * Tested methods:
	 * 		add(E e), set (int index, E element), remove (int index), isEmpty()
	 */
	@Test
	public void goolLibraryJavaListToCppVectorTestSetRemove() throws Exception {
		MAIN_CLASS_NAME = "TestTypeListToVectorCpp_TestSetRemove";
		String input = "import java.util.List;"
				+ "import java.util.ArrayList;"
				+ TestHelperJava
				.surroundWithClassMainFile(
						"/* creation of a list -- remove + set  */"
								+ "try{"
								+ "List<String> list = new ArrayList<String>();"
								+ 

										"list.add(\"toto\");"
										+ "list.set(0, \"tata\");"
										+

										"if(\"tata\" == list.get(0)){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\"); }"
										+ "list.remove(0);"
										+

										"if(list.isEmpty()){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\"); }"
										+ "}catch(Exception e){System.out.println(e.toString())}",
										MAIN_CLASS_NAME);
		String expected = "true" + "true";
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

	//@AfterClass
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
