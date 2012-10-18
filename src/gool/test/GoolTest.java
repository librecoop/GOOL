package gool.test;

import gool.platform.Platform;
import gool.platform.cpp.CppPlatform;
import gool.platform.csharp.CSharpPlatform;
import gool.platform.java.JavaPlatform;
import gool.util.Helper;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

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
			System.out.println(platform + " Result: " + result);
			Assert.assertEquals(String.format("The platform %s", platform), expected, result);
		}
		protected String compileAndRun(Platform platform) throws Exception {
System.out.println(input);
			String cleanOutput = cleanOutput(Helper.generateCompileRun(
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
		Properties properties = new Properties();
		properties.put("gool_library", "./gool.jar");
		properties.put("gool_out_dir", "./output/gool/");
		properties.put("java_out_dir", "./output/java/");
		properties.put("csharp_out_dir", "./output/csharp/");
		properties.put("cpp_out_dir", "./output/cpp/");
		gool.util.Settings.getInstance().load(properties);
	}

	@Test
	public void helloWorld() throws Exception {
		String input = Helper.surroundWithClassMain(
				"Gool.print(\"Hello World\");", MAIN_CLASS_NAME);
		String expected = "Hello World";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleAddition() throws Exception {
		String input = Helper.surroundWithClassMain("Gool.print(2 + 2);",
				MAIN_CLASS_NAME);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleIf() throws Exception {
		String input = Helper
				.surroundWithClassMain(
						"boolean b = true; if (b) { Gool.print(2 + 2);} else { Gool.print(2 + 5); }",
						MAIN_CLASS_NAME);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleFor() throws Exception {
		String input = Helper
				.surroundWithClassMain(
						"int total = 0; for(int i = 0; i < 4; i++){ total ++;} Gool.print(total);",
						MAIN_CLASS_NAME);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void listAddGet() throws Exception {
		String input = Helper
				.surroundWithClassMain(
						"List<Integer> l = new List<Integer>(); l.add(4); Gool.print(l.get(0));",
						MAIN_CLASS_NAME);
		System.out.println(input);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void mapAddGet() throws Exception {
		// String input =
		// Helper.surroundWithClassMain("Map<String, Integer > m = new Map<String, Integer>();",
		// "Test");
		String input = Helper
				.surroundWithClassMain(
						"String four = \"four\"; Map<String, Integer > m = new Map<String, Integer>(); m.put(four,4); Gool.print(m.get(four));",
						MAIN_CLASS_NAME);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleNew() throws Exception {
		String input = Helper
				.surroundWithClass(
						"public void print(){Gool.print(2 + 2);} public static void main(String[] args){ Test t = new Test(); t.print();}",
						MAIN_CLASS_NAME, "");
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleTwoClasses() throws Exception {
		String input = Helper.surroundWithClassMain(
				"Printer p = new Printer(); p.print();", MAIN_CLASS_NAME);
		input += "\n"
				+ Helper.surroundWithClass(
						"public void print(){Gool.print(2 + 2);}", "Printer", "");
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleForEach() throws Exception {
		String input = Helper
				.surroundWithClassMain(
						"Integer total = 0;"
								+ " List<Integer> l = new List<Integer>();"
								+ " l.add(-2); l.add(-1);l.add(0); l.add(1); l.add(2);l.add(4);"
								+ " for(Integer i : l){" + "total = total + i;"
								+ "}" + "Gool.print(total);", MAIN_CLASS_NAME);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void mapForEach() throws Exception {
		String input = Helper.surroundWithClassMain("Integer total = 0;"
				+ " Map<Integer, Integer> m = new Map<Integer, Integer>();"
				+ " m.put(0, 1); m.put(2, 3);"
				+ " for(Map.Entry<Integer, Integer> entry : m){"
				+ "total = total + entry.getKey();"
				+ "total = total + entry.getValue();" + "}"
				+ "Gool.print(total);", MAIN_CLASS_NAME);
		String expected = "6";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void listWithDifferentTypeElement() throws Exception {
		String input = Helper
				.surroundWithClassMain(
						"List l = new List();l.add(1);l.add(\"hola\");Gool.print(l.size());",
						MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(input, "2");
	}

	@Test
	public void mapWithoutTypes() throws Exception {
		try {
			String input = Helper
					.surroundWithClassMain(
							"Map m = new Map();m.put(0, 1);m.put(\"hola\", 2);Gool.print(m.size());",
							MAIN_CLASS_NAME);
			compareResultsDifferentPlatforms(input, "2");
		} 
		catch (Exception e) {
			if (e.getCause() != null && e.getCause().getClass().equals(IllegalStateException.class)) {
				return;
			}
			System.out.println(e);
		}
		Assert.fail("Maps with object keys are not allowed in C++.");
	}

	@Test
	public void removeElementsFromUntypedList() throws Exception {
		String input = Helper
				.surroundWithClassMain(
						"List l = new List();l.add(\"\");l.add(\"hola\");l.remove(\"hola\");Gool.print(l.size());",
						MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(input, "1");
	}

	@Test
	public void removeElementsFromIntegerList() throws Exception {
		String input = Helper
				.surroundWithClassMain(
						"List<Integer> l = new List<Integer>();l.add(1);l.add(4);l.removeAt(1);Gool.print(l.size());",
						MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(input, "1");
	}

	@Test
	public void removeElementsFromMap() throws Exception {
		String input = Helper
				.surroundWithClassMain(
						"Map<Integer, Integer> m = new Map<Integer, Integer>();m.put(1, 2);m.put(2, 3);m.remove(2);Gool.print(m.size());",
						MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(input, "1");
	}
	
	@Test
	public void isEmptyList() throws Exception {
		String input = Helper
				.surroundWithClassMain(
						"List l = new List();l.add(\"hola\");l.remove(\"hola\");Gool.print(l.isEmpty());",
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
		String input = Helper
				.surroundWithClassMain(
						"List l = new List();l.add(\"hola\");l.remove(\"hola\");l.add(\"hola\");Gool.print(l.contains(\"hola\"));",
						MAIN_CLASS_NAME);
		Assert.fail("Not implemented");
	}
	
	@Test
	public void classWithAttributes() throws Exception {
		String input = "class Test {" +
				"public int z; public Test(int i){this.z=i+2;}" +
				"public static void main(String[] args){" +
				"Gool.print(new Test(5).z);"+
				"}}";
		compareResultsDifferentPlatforms(input, "7");
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
