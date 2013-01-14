package gool.test;

import gool.generator.common.Platform;
import gool.generator.cpp.CppPlatform;
import gool.generator.csharp.CSharpPlatform;
import gool.generator.java.JavaPlatform;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import logger.Log;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class GoolTest {
	private static class GoolTestExecutor {
		private static final String CLEAN_UP_REGEX = "Note:.*?[\r\n]|(\\w+>\\s)|[\\r\\n]+";
		private String input;
		private String expected;
		public GoolTestExecutor(String input, String expected) {
			this.input = input;
			this.expected = expected;
		}
		public void compare(Platform platform) throws Exception {
			String result = compileAndRun(platform);
			Log.i(platform + " Result: " + result);
			Assert.assertEquals(String.format("The platform %s", platform), expected, result);
		}
		protected String compileAndRun(Platform platform) throws Exception {
			String cleanOutput = cleanOutput(TestHelper.generateCompileRun(
					platform, input, MAIN_CLASS_NAME));
			return cleanOutput;
		}
		
		private static String cleanOutput(String result) {
			return result.replaceAll(CLEAN_UP_REGEX, "")
					.trim();
		}
	}
	
	private static final String MAIN_CLASS_NAME = "Test";
	private List<Platform> platforms =
	 Arrays.asList(JavaPlatform.getInstance(), CSharpPlatform.getInstance(), CppPlatform.getInstance());
//	private List<Platform> platforms = Arrays.asList(CppPlatform.getInstance());

	@BeforeClass
	public static void init() {
	}

	@Test
	public void helloWorld() throws Exception {
		String input = TestHelper.surroundWithClassMain(
				"System.out.println(\"Hello World\");", MAIN_CLASS_NAME);
		String expected = "Hello World";
		compareResultsDifferentPlatforms(input, expected);
	}


	@Test
	public void simpleAddition() throws Exception {
		String input = TestHelper.surroundWithClassMain("System.out.println(2 + 2);",
				MAIN_CLASS_NAME);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleIf() throws Exception {
		String input = TestHelper
				.surroundWithClassMain(
						"boolean b = true; if (b) { System.out.println(2 + 2);} else { System.out.println(2 + 5); }",
						MAIN_CLASS_NAME);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleFor() throws Exception {
		String input = TestHelper
				.surroundWithClassMain(
						"int total = 0; for(int i = 0; i < 4; i++){ total ++;} System.out.println(total);",
						MAIN_CLASS_NAME);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void listAddGet() throws Exception {
		String input = 
				"import gool.imports.java.util.ArrayList;\n" + 
				TestHelper
				.surroundWithClassMain(
						"ArrayList<Integer> l = new ArrayList<Integer>(); l.add(4); System.out.println(l.get(0));",
						MAIN_CLASS_NAME);
		Log.i(input);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void mapAddGet() throws Exception {
		// String input =
		// TestHelper.surroundWithClassMain("HashMap<String, Integer > m = new HashMap<String, Integer>();",
		// "Test");
		String input = 
				"import gool.imports.java.util.HashMap;\n" + 

				TestHelper
				.surroundWithClassMain(
						"String four = \"four\"; HashMap<String, Integer > m = new HashMap<String, Integer>(); m.put(four,4); System.out.println(m.get(four));",
						MAIN_CLASS_NAME);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleNew() throws Exception {
		String input = TestHelper
				.surroundWithClass(
						"public void print(){System.out.println(2 + 2);} public static void main(String[] args){ Test t = new Test(); t.print();}",
						MAIN_CLASS_NAME, "");
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleTwoClasses() throws Exception {
		String input = TestHelper.surroundWithClassMain(
				"Printer p = new Printer(); p.print();", MAIN_CLASS_NAME);
		input += "\n"
				+ TestHelper.surroundWithClass(
						"public void print(){System.out.println(2 + 2);}", "Printer", "");
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleForEach() throws Exception {
		String input = 
				"import gool.imports.java.util.ArrayList;\n" + 
				TestHelper
				.surroundWithClassMain(
						"Integer total = 0;"
								+ " ArrayList<Integer> l = new ArrayList<Integer>();"
								+ " l.add(-2); l.add(-1);l.add(0); l.add(1); l.add(2);l.add(4);"
								+ " for(Integer i : l){" + "total = total + i;"
								+ "}" + "System.out.println(total);", MAIN_CLASS_NAME);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void mapForEach() throws Exception {
		String input = 
				"import gool.imports.java.util.HashMap;\n" + 
				TestHelper.surroundWithClassMain("Integer total = 0;"
				+ " HashMap<Integer, Integer> m = new HashMap<Integer, Integer>();"
				+ " m.put(0, 1); m.put(2, 3);"
				+ " for(HashMap.Entry<Integer, Integer> entry : m){"
				+ "total = total + entry.getKey();"
				+ "total = total + entry.getValue();" + "}"
				+ "System.out.println(total);", MAIN_CLASS_NAME);
		String expected = "6";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void listWithDifferentTypeElement() throws Exception {
		String input = 
				"import gool.imports.java.util.ArrayList;\n" + 
				TestHelper
				.surroundWithClassMain(
						"ArrayList l = new ArrayList();l.add(1);l.add(\"hola\");System.out.println(l.size());",
						MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(input, "2");
	}

	@Test
	public void mapWithoutTypes() throws Exception {
		try {
			String input = 
					"import gool.imports.java.util.HashMap;\n" + 
					TestHelper
					.surroundWithClassMain(
							"HashMap m = new HashMap();m.put(0, 1);m.put(\"hola\", 2);System.out.println(m.size());",
							MAIN_CLASS_NAME);
			compareResultsDifferentPlatforms(input, "2");
		} 
		catch (Exception e) {
			if (e.getCause() != null && e.getCause().getClass().equals(IllegalStateException.class)) {
				return;
			}
			Log.e(e);
		}
		Assert.fail("Maps with object keys are not allowed in C++.");
	}

	@Test
	public void removeElementsFromUntypedList() throws Exception {
		String input = 
				"import gool.imports.java.util.ArrayList;\n" + 
				TestHelper
				.surroundWithClassMain(
						"ArrayList l = new ArrayList();l.add(\"\");l.add(\"hola\");l.remove(\"hola\");System.out.println(l.size());",
						MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(input, "1");
	}

	@Test
	public void removeElementsFromIntegerList() throws Exception {
		String input = 
				"import gool.imports.java.util.ArrayList;\n" + 
				TestHelper
				.surroundWithClassMain(
						"ArrayList<Integer> l = new ArrayList<Integer>();l.add(1);l.add(4);l.removeAt(1);System.out.println(l.size());",
						MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(input, "1");
	}

	@Test
	public void removeElementsFromMap() throws Exception {
		String input = 
				"import gool.imports.java.util.HashMap;\n" + 
				TestHelper
				.surroundWithClassMain(
						"HashMap<Integer, Integer> m = new HashMap<Integer, Integer>();m.put(1, 2);m.put(2, 3);m.remove(2);System.out.println(m.size());",
						MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(input, "1");
	}
	
	@Test
	public void isEmptyList() throws Exception {
		String input = 
				"import gool.imports.java.util.ArrayList;\n" + 
				TestHelper
				.surroundWithClassMain(
						"ArrayList l = new ArrayList();l.add(\"hola\");l.remove(\"hola\");System.out.println(l.isEmpty());",
						MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(new GoolTestExecutor(input, "true"){
			@Override
			protected String compileAndRun(Platform platform) throws Exception {
				String output = super.compileAndRun(platform).toLowerCase();
				
				// C++ does not have booleans
				if ("1".equals(output)) {
					output = "true";
				} else if ("0".equals(output)) {
					output = "false";
				}
				
				return output;
			}
		});
	}
	
	@Test
	public void listContainsElement() throws Exception {
		String input = 
				"import gool.imports.java.util.ArrayList;\n" + 
				TestHelper
				.surroundWithClassMain(
						"ArrayList l = new ArrayList();l.add(\"hola\");l.remove(\"hola\");l.add(\"hola\");System.out.println(l.contains(\"hola\"));",
						MAIN_CLASS_NAME);
		Assert.fail("Not implemented");
	}
	
	@Test
	public void classWithAttributes() throws Exception {
		String input = "class Test {" +
				"public int z; public Test(int i){this.z=i+2;}" +
				"public static void main(String[] args){" +
				"System.out.println(new Test(5).z);"+
				"}}";
		compareResultsDifferentPlatforms(input, "7");
	}

	@Test
	public void unknownOperator() throws Exception {
		String input = TestHelper
				.surroundWithClassMain(
						"int total = 1 ^ 0; System.out.println(total);",
						MAIN_CLASS_NAME);
		String expected = "";
		compareResultsDifferentPlatforms(input, "expected");
	}

	private void compareResultsDifferentPlatforms(String input,
			String expected) throws Exception {
		compareResultsDifferentPlatforms(new GoolTestExecutor(input, expected));
	}
	
	private void compareResultsDifferentPlatforms(GoolTestExecutor executor) throws Exception {
		for (Platform platform : platforms) {
			executor.compare(platform);
		}
	}	
}
