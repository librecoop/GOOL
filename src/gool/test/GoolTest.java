package gool.test;

import gool.Settings;
import gool.generator.android.AndroidPlatform;
import gool.generator.common.Platform;
import gool.generator.cpp.CppPlatform;
import gool.generator.csharp.CSharpPlatform;
import gool.generator.java.JavaPlatform;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class GoolTest {
	private static Logger logger = Logger.getLogger(GoolTest.class);

	private static class GoolTestExecutor {
		private static final String CLEAN_UP_REGEX = "Note:.*?[\r\n]|(\\w+>\\s)|[\\r\\n]+";
		private String input;
		private String expected;

		public GoolTestExecutor(String input, String expected) {
			this.input = input;
			this.expected = expected;
		}

		public void compare(Platform platform) throws Exception {
			// This inserts a package which is mandatory for android
			// TODO Not the ideal place to put it also com.test should be in the
			// properties file
			if (platform instanceof AndroidPlatform) {
				this.input = "package com.test; " + input;
			}
			String result = compileAndRun(platform);
			logger.info(platform.getName() +" Expected Value:"+expected+ ", Result: " + result);
			Assert.assertEquals(String.format("The platform %s", platform),
					expected, result);
		}

		protected String compileAndRun(Platform platform) throws Exception {
			String cleanOutput = cleanOutput(TestHelper.generateCompileRun(
					platform, input, MAIN_CLASS_NAME));
			return cleanOutput;
		}

		private static String cleanOutput(String result) {
			return result.replaceAll(CLEAN_UP_REGEX, "").trim();
		}
	}

	private static final String MAIN_CLASS_NAME = "Test";

	@BeforeClass
	public static void init() {
	}

	@Test
	public void helloWorld() throws Exception {
		String input = TestHelper.surroundWithClassMain(
				"try {\n " +
				"System.out.println(\"Hello World\");" 
				//+"throw new Exception(\"throwing...\");\n"
						+"\n }  catch(Exception testExcep) {\n System.out.println(\"notfound\");\n }"
				, MAIN_CLASS_NAME);
		String expected = "Hello World";
		logger.info("START: HELLO WORLD test, expected value: "+expected);
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleChar() throws Exception {
		String input = TestHelper.surroundWithClassMain(
				"char testChar = 'A'; System.out.println(testChar);",
				MAIN_CLASS_NAME);
		String expected = "A";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleAddition() throws Exception {
		String input = TestHelper.surroundWithClassMain(
				"System.out.println(2 + 2);", MAIN_CLASS_NAME);
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
		String input = "import gool.imports.java.util.ArrayList;\n"
				+ TestHelper
						.surroundWithClassMain(
								"ArrayList<Integer> l = new ArrayList<Integer>(); l.add(4); System.out.println(l.get(0));",
								MAIN_CLASS_NAME);
		logger.info(input);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void mapAddGet() throws Exception {
		// String input =
		// TestHelper.surroundWithClassMain("HashMap<String, Integer > m = new HashMap<String, Integer>();",
		// "Test");
		String input = "import gool.imports.java.util.HashMap;\n"
				+

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
						"public void print()  {System.out.println(2 + 2);}",
						"Printer", "");
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleForEach() throws Exception {
		String input = "import gool.imports.java.util.ArrayList;\n"
				+ TestHelper
						.surroundWithClassMain(
								"Integer total = 0;"
										+ " ArrayList<Integer> l = new ArrayList<Integer>();"
										+ " l.add(-2); l.add(-1);l.add(0); l.add(1); l.add(2);l.add(4);"
										+ " for(Integer i : l){"
										+ "total = total + i;" + "}"
										+ "System.out.println(total);",
								MAIN_CLASS_NAME);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void mapForEach() throws Exception {
		String input = "import gool.imports.java.util.HashMap;\n"
				+ TestHelper
						.surroundWithClassMain(
								"Integer total = 0;"
										+ " HashMap<Integer, Integer> m = new HashMap<Integer, Integer>();"
										+ " m.put(0, 1); m.put(2, 3);"
										+ " for(HashMap.Entry<Integer, Integer> entry : m){"
										+ "total = total + entry.getKey();"
										+ "total = total + entry.getValue();"
										+ "}" + "System.out.println(total);",
								MAIN_CLASS_NAME);
		String expected = "6";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void listWithDifferentTypeElement() throws Exception {
		String input = "import gool.imports.java.util.ArrayList;\n"
				+ TestHelper
						.surroundWithClassMain(
								"ArrayList l = new ArrayList();l.add(1);l.add(\"hola\");System.out.println(l.size());",
								MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(input, "2");
	}

	@Test
	public void mapWithoutTypes() throws Exception {
		try {
			String input = "import gool.imports.java.util.HashMap;\n"
					+ TestHelper
							.surroundWithClassMain(
									"HashMap m = new HashMap();m.put(0, 1);m.put(\"hola\", 2);System.out.println(m.size());",
									MAIN_CLASS_NAME);
			compareResultsDifferentPlatforms(input, "2");
		} catch (Exception e) {
			if (e.getCause() != null
					&& e.getCause().getClass()
							.equals(IllegalStateException.class)) {
				return;
			}
			logger.error(e);
		}
		Assert.fail("Maps with object keys are not allowed in C++.");
	}

	@Test
	public void removeElementsFromUntypedList() throws Exception {
		String input = "import gool.imports.java.util.ArrayList;\n"
				+ TestHelper
						.surroundWithClassMain(
								"ArrayList l = new ArrayList();l.add(\"\");l.add(\"hola\");l.remove(\"hola\");System.out.println(l.size());",
								MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(input, "1");
	}

	@Test
	public void removeElementsFromIntegerList() throws Exception {
		String input = "import gool.imports.java.util.ArrayList;\n"
				+ TestHelper
						.surroundWithClassMain(
								"ArrayList<Integer> l = new ArrayList<Integer>();l.add(1);l.add(4);l.removeAt(1);System.out.println(l.size());",
								MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(input, "1");
	}

	@Test
	public void removeElementsFromMap() throws Exception {
		String input = "import gool.imports.java.util.HashMap;\n"
				+ TestHelper
						.surroundWithClassMain(
								"HashMap<Integer, Integer> m = new HashMap<Integer, Integer>();m.put(1, 2);m.put(2, 3);m.remove(2);System.out.println(m.size());",
								MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(input, "1");
	}

	@Test
	public void isEmptyList() throws Exception {
		String input = "import gool.imports.java.util.ArrayList;\n"
				+ TestHelper
						.surroundWithClassMain(
								"ArrayList l = new ArrayList();l.add(\"hola\");l.remove(\"hola\");System.out.println(l.isEmpty());",
								MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(new GoolTestExecutor(input, "true") {
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
		String input = "import gool.imports.java.util.ArrayList;\n"
				+ TestHelper
						.surroundWithClassMain(
								"ArrayList l = new ArrayList();l.add(\"hola\");l.remove(\"hola\");l.add(\"hola\");System.out.println(l.contains(\"hola\"));",
								MAIN_CLASS_NAME);
		Assert.fail("Not implemented");
	}

	@Test
	public void classWithAttributes() throws Exception {
		String input = "class Test {"
				+ "public int z; public Test(int i){this.z=i+2;}"
				+ "public static void main(String[] args){"
				+ "System.out.println(new Test(5).z);" + "}}";
		compareResultsDifferentPlatforms(input, "7");
	}

	@Test
	public void unknownOperator() throws Exception {
		String input = TestHelper.surroundWithClassMain(
				"int total = 1 ^ 0; System.out.println(total);",
				MAIN_CLASS_NAME);
		String expected = "";
		compareResultsDifferentPlatforms(input, "expected");
	}
	
	/**
	 * First test using a file as a FileWriter constructor argument
	 * 
	 */

	@Test
	public void fileTest() throws Exception {
		//String readFile = Settings.get("read_file_path");
		//String readFile = Settings.get("read_file_path");
		String writeFile = Settings.get("write_file_path");
		String input = "import gool.imports.java.io.BufferedReader;\n"
				+ "import gool.imports.java.io.FileReader;\n"
				+ "import gool.imports.java.io.FileWriter;\n"
				+ "import gool.imports.java.io.BufferedWriter;\n"
				+ "import gool.imports.java.io.File;\n"
				+ "import gool.imports.java.io.IOException;\n"
				+ TestHelper
						.surroundWithClassMain(
								"\n try{ \n " + "File b = new File (\""+writeFile+"\");\n if(b.exists() == true) \n {b.delete();} \n"
										+"BufferedWriter bw = new BufferedWriter(new FileWriter(b)); \n"
										+"bw.write(\"line1\"); bw.newLine();bw.write(65); \n bw.flush(); \n bw.close(); \n"
										+"BufferedReader br = new BufferedReader(new FileReader(b)); \n"
										+" String testString = br.readLine();char c = (char)br.read(); \n"
										+"br.close();\n"
										+"  \n System.out.println(testString+c);\n "
								
								
								+"} catch(IOException iox) \n { System.out.println(\"iox\");} \n catch(Exception e) \n {System.out.println(\"ex\");}\n",
								MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(input, "line1A");
	}
	
	/**
	 * Second test using a string as a FileWriter constructor argument
	 * 
	 */

	@Test
	public void fileTestSecond() throws Exception {
		//String readFile = Settings.get("read_file_path");
		//String readFile = Settings.get("read_file_path");
		String writeFile = Settings.get("write_file_path");
		String input = "import gool.imports.java.io.BufferedReader;\n"
				+ "import gool.imports.java.io.FileReader;\n"
				+ "import gool.imports.java.io.FileWriter;\n"
				+ "import gool.imports.java.io.BufferedWriter;\n"
				+ "import gool.imports.java.io.File;\n"
				+ "import gool.imports.java.io.FileNotFoundException;\n"
				+ "import gool.imports.java.io.EOFException;\n"
				+ TestHelper
						.surroundWithClassMain(
								"\n try{ \n " + "File b = new File (\""+writeFile+"\");\n if(b.exists() == true) \n {b.delete();} \n"
										+"BufferedReader br = new BufferedReader(new FileReader(\""+writeFile+"\")); \n"
										+" String testString = br.readLine();char c = (char)br.read(); \n"
										+"br.close();\n"
										+"  \n System.out.println(testString+c);\n "
								
								+"} catch(EOFException eof) \n { System.out.println(\"eof\");} "
								+" catch(FileNotFoundException fnf) \n { System.out.println(\"notfound\");} "
								+" catch(Exception ex) \n {System.out.println(\"ex\");}",
								MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(input, "eof");
	}

	@Test
	public void fileTestAppend() throws Exception {
		//String readFile = Settings.get("read_file_path");
		//String readFile = Settings.get("read_file_path");
		String writeFile = Settings.get("write_file_path");
		String input = "import gool.imports.java.io.BufferedReader;\n"
				+ "import gool.imports.java.io.FileReader;\n"
				+ "import gool.imports.java.io.FileWriter;\n"
				+ "import gool.imports.java.io.BufferedWriter;\n"
				+ "import gool.imports.java.io.File;\n"
				+ TestHelper
						.surroundWithClassMain(
								"\n try{ \n " + "File b = new File (\""+writeFile+"\");\n if(b.exists() == true) \n {b.delete();} \n"
										+"BufferedWriter bw = new BufferedWriter(new FileWriter(b)); \n"
										+"bw.write(\"line1\"); bw.newLine();bw.write(65); \n bw.flush(); \n bw.close(); \n"
										+"BufferedWriter bw2 = new BufferedWriter(new FileWriter(b, true)); \n"
										+"bw2.write(\"write2\");  \n bw2.flush(); \n bw2.close(); \n"
										+"BufferedReader br = new BufferedReader(new FileReader(b)); \n"
										+" String testString = br.readLine(); \n"
										+" testString = testString + br.readLine(); \n"
										+"br.close();\n"
										+"  \n System.out.println(testString);\n "
								
								
								+"} catch(Exception ex) \n {System.out.println(\"ex\");}",
								MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(input, "line1Awrite2");
		Assert.fail("Append is not supported by bufferedreader at the moment");
	}
	
	@Test
	public void exceptionThrowTest() throws Exception {
		String input = "import gool.imports.java.io.IOException;\n" +TestHelper.surroundWithClassMain(
				"try {\n Test t=new Test(); t.print();\n}\n" +
				"catch(IOException ioe) {\n System.out.println(\"ioe\");\n}\n" +
				"catch(Exception e) {\n System.out.println(\"e\");\n}\n}"		
				+"\n public void print() throws IOException, Exception {System.out.println(2 + 2);"		
				, MAIN_CLASS_NAME);
		
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}
	private void compareResultsDifferentPlatforms(String input, String expected)
			throws Exception {
		compareResultsDifferentPlatforms(new GoolTestExecutor(input, expected));
	}

	private void compareResultsDifferentPlatforms(GoolTestExecutor executor)
			throws Exception {
			Platform platform =CppPlatform.getInstance();
			logger.info("START: platform: " +platform.getName());
			executor.compare(platform);
			logger.info("FINISH platform: " +platform.getName());
	}
}
