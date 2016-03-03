package gool.test.lib;


import gool.generator.common.Platform;
import gool.generator.cpp.CppPlatform;
import gool.generator.csharp.CSharpPlatform;
import gool.generator.java.JavaPlatform;
import gool.generator.objc.ObjcPlatform;
import gool.generator.python.PythonPlatform;
import gool.test.TestHelperJava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
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

	 (Platform) JavaPlatform.getInstance(),
	 (Platform) CSharpPlatform.getInstance(),
	 (Platform) CppPlatform.getInstance(),
	 (Platform) PythonPlatform.getInstance()// ,
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

	private static final String MAIN_CLASS_NAME = "TestTypeListToVectorCpp";

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
								MAIN_CLASS_NAME + "_TestAddContains");
		String expected = "true" + "true";
		excludePlatformForThisTest((Platform) CppPlatform.getInstance());
		excludePlatformForThisTest((Platform) PythonPlatform.getInstance());
		compareResultsDifferentPlatforms(input, expected, 1);
	}

	/*
	 * Creates a list to be translated into a vector (CPP only)
	 * Tested methods:
	 * 		add(E e), remove(object o), isEmpty()
	 */
	@Test
	public void goolLibraryJavaListToCppVectorTestRemove() throws Exception {
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
								MAIN_CLASS_NAME + "_TestRemove");
		String expected = "true" + "true";
		compareResultsDifferentPlatforms(input, expected, 1);
	}
	
	/*
	 * Creates a list to be translated into a vector (CPP only)
	 * Tested methods:
	 * 		add(E e), add(int Index, E e), get(int Index)
	 */
	@Test
	public void goolLibraryJavaListToCppVectorTestAddGet() throws Exception {
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
								MAIN_CLASS_NAME + "_TestAddGet");
		String expected = "true" + "true";
		compareResultsDifferentPlatforms(input, expected, 1);
	}
	
	/*
	 * Creates a list to be translated into a vector (CPP only)
	 * Tested methods:
	 * 		add(E e), size(), clear(), isEmpty()
	 */
	@Test
	public void goolLibraryJavaListToCppVectorTestSizeClear() throws Exception {
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
								MAIN_CLASS_NAME + "_TestSizeClear");
		String expected = "true" + "true" + "true";

		compareResultsDifferentPlatforms(input, expected, 1);
	}
	
	/*
	 * Creates a list to be translated into a vector (CPP only)
	 * Tested methods:
	 * 		add(E e), equals(Object o)
	 */
	@Test
	public void goolLibraryJavaListToCppVectorTestEquals() throws Exception {
		String input = "import java.util.List;"
				+ "import java.util.ArrayList;"
				+ TestHelperJava
						.surroundWithClassMainFile(
								"/* creation of a list -- equals + indexOf */"
										+ "try{"
										+ "List<String> list = new ArrayList<String>();"
										+ "List<String> list2 = new ArrayList<String>();"
										+

										"list.add(\"toto\");"
										+ "list.add(\"tata\");"
										+ "list2.add(\"toto\");"
										+ "list2.add(\"tata\");"
										+

										"if(list.equals(list2)){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\"); }"
										+ "list2.add(\"titi\");"
										+
										
										"if(list.equals(list2)){ System.out.println(\"true\"); }"
										+ "else{ System.out.println(\"false\"); }"
										+ "}catch(Exception e){System.out.println(e.toString())}",
								MAIN_CLASS_NAME + "_TestEquals");
		String expected = "true" + "false";
	//	excludePlatformForThisTest((Platform) CSharpPlatform.getInstance());
		excludePlatformForThisTest((Platform) CppPlatform.getInstance());
		excludePlatformForThisTest((Platform) PythonPlatform.getInstance());
		compareResultsDifferentPlatforms(input, expected, 1);
	}
	
	/*
	 * Creates a list to be translated into a vector (CPP only)
	 * Tested methods:
	 * 		add(E e), indexOf(Object o)
	 */
	@Test
	public void goolLibraryJavaListToCppVectorTestIndexOf() throws Exception {
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
								MAIN_CLASS_NAME + "_TestIndexOf");
		String expected = "true";
//		excludePlatformForThisTest((Platform) CSharpPlatform.getInstance());
//		excludePlatformForThisTest((Platform) CppPlatform.getInstance());
//		excludePlatformForThisTest((Platform) PythonPlatform.getInstance());
		compareResultsDifferentPlatforms(input, expected, 1);
	}

	/*
	 * Creates a list to be translated into a vector (CPP only)
	 * Tested methods:
	 * 		add(E e), set (int index, E element), remove (int index), isEmpty()
	 */
	@Test
	public void goolLibraryJavaListToCppVectorTestSetRemove() throws Exception {
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
								MAIN_CLASS_NAME + "_TestSetRemove");
		String expected = "true" + "true";
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
