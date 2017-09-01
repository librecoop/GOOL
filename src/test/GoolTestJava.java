/*
 * Copyright 2010 Pablo Arrighi, Alex Concha, Miguel Lezama for version 1.
 * Copyright 2013 Pablo Arrighi, Miguel Lezama, Kevin Mazet for version 2.    
 *
 * This file is part of GOOL.
 *
 * GOOL is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, version 3.
 *
 * GOOL is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License version 3 for more details.
 *
 * You should have received a copy of the GNU General Public License along with GOOL,
 * in the file COPYING.txt.  If not, see <http://www.gnu.org/licenses/>.
 */

package gool.test;

import gool.Settings;
import gool.generator.common.Platform;
import gool.generator.cpp.CppPlatform;
import gool.generator.csharp.CSharpPlatform;
import gool.generator.java.JavaPlatform;
import gool.generator.python.PythonPlatform;
import gool.generator.objc.ObjcPlatform;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GoolTestJava {

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

	private static String MAIN_CLASS_NAME = "Test";

	private List<Platform> testNotImplementedOnPlatforms = new ArrayList<Platform>();

	private void excludePlatformForThisTest(Platform platform) {
		testNotImplementedOnPlatforms.add(platform);
	}

	@BeforeClass
	public static void init() {
	}

	@Before
	@Test
	public void helloWorld() throws Exception {
		MAIN_CLASS_NAME = "TestHelloWorld";
		String input = TestHelperJava.surroundWithClassMain(
				"System.out.println(\"Hello World\");", MAIN_CLASS_NAME);
		String expected = "Hello World";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void goolLibraryTest1() throws Exception {
		MAIN_CLASS_NAME = "testGoolLibraryTest1";
		String input = "import java.io.File;" 
				+ TestHelperJava.surroundWithClassMain(
						"/* création puis suppression d'un fichier qui n'existait pas */"
								+ "try{"
								+ "File f = new File(\"../testGool.txt\");"
								+ "if(f.exists()){ System.out.println(\"true\"); }"
								+ "else{ System.out.println(\"false\");}"
								+ "if(f.createNewFile()){ System.out.println(\"true\"); }"
								+ "else{ System.out.println(\"false\"); }"
								+ "if(f.exists()){ System.out.println(\"true\"); }"
								+ "else{ System.out.println(\"false\");}"
								+ "if(f.delete()){ System.out.println(\"true\"); }"
								+ "else{ System.out.println(\"false\"); }"
								+ "if(f.exists()){ System.out.println(\"true\"); }"
								+ "else{ System.out.println(\"false\");}"
								+ "}catch(Exception e){"
								+ "System.out.println(\"error\");}",
								MAIN_CLASS_NAME
						);
		String expected = "false" + "true" + "true" + "true" + "false";

		// Matching of the GoolFile library class and of its method
		// work only for the Java target language at the moment,
		// so we exclude the other platforms for this test.
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void goolLibraryTest2() throws Exception {
		MAIN_CLASS_NAME = "testGoolLibraryTest2";
		String input = "import java.io.File;"
				+ "import java.io.BufferedReader;"
				+ "import java.io.FileReader;"
				+ "import java.io.BufferedWriter;"
				+ "import java.io.FileWriter;"
				+ TestHelperJava.surroundWithClassMain(
						"/* Creation d'un fichier, écriture, lecture, puis suppression du fichier. */"
								+ "try{"
								+ "File f = new File(\"../testGool.txt\");"
								+ "f.createNewFile();"
								+ "FileWriter fw=new FileWriter(f);"
								+ "BufferedWriter bw=new BufferedWriter(fw);"
								+ "bw.write('a'); bw.write('b'); bw.close();"
								+ "FileReader fr=new FileReader(f);"
								+ "BufferedReader br=new BufferedReader(fr);"
								+ "char c1=(char)br.read(), c2=(char)br.read(); br.close();"
								+ "System.out.println(c1+\"\"+c2);"
								+ "f.delete();" + "}catch(Exception e){"
								+ "System.out.println(\"error\");}",
								MAIN_CLASS_NAME
						);
		String expected = "ab";

		// Matching of the io GOOL library with classes and methods
		// of the output language work only for the Java target at the moment,
		// so we exclude the other platforms for this test.
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void goolLibraryTest3() throws Exception {
		MAIN_CLASS_NAME = "testGoolLibraryTest3";
		String input = "import java.io.File;"
				+ "import java.io.BufferedReader;"
				+ "import java.io.FileReader;"
				+ "import java.io.BufferedWriter;"
				+ "import java.io.FileWriter;"
				+ TestHelperJava.surroundWithClassMain(
						"/* Creation d'un fichier, écriture, lecture, puis suppression du fichier. */"
								+ "try{"
								+ "File f = new File(\"../testGool.txt\");"
								+ "f.createNewFile();" 
								+ "FileWriter fw=new FileWriter(f);"
								+ "BufferedWriter bw=new BufferedWriter(fw);"
								+ "String s=\"hello world\\n42\\n\";"
								+ "bw.write(s,0,s.length()); bw.close();" 
								+ "FileReader fr=new FileReader(f);"
								+ "BufferedReader br=new BufferedReader(fr);"
								+ "String line=br.readLine();"
								+ "while(line!=null){"
								+ "System.out.println(line);"
								+ "line=br.readLine();" + "}" + "br.close();"
								+ "f.delete();" + "}catch(Exception e){" 
								+ "System.out.println(\"error\");}",
								MAIN_CLASS_NAME
						);
		String expected = "hello world42";

		// Matching of the io GOOL library with classes and methods
		// of the output language work only for the Java target at the moment,
		// so we exclude the other platforms for this test.
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleTryCatch() throws Exception {
		MAIN_CLASS_NAME = "testSimpleTryCatch";
		String input = TestHelperJava.surroundWithClassMain("try {\n "
				+ "System.out.println(\"hello \");"
				+ "throw(new Exception());"
				+ "}"
				+ "catch(Exception e){" + "System.out.println(\"world\");"
				+ "}", MAIN_CLASS_NAME);
		String expected = "hello world";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleChar() throws Exception {
		MAIN_CLASS_NAME = "testSimpleChar";
		String input = TestHelperJava.surroundWithClassMain(
				"char testChar = 'A'; System.out.println(testChar);",
				MAIN_CLASS_NAME);
		String expected = "A";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleAddition() throws Exception {
		MAIN_CLASS_NAME = "testSimpleAddition";
		String input = TestHelperJava.surroundWithClassMain(
				"System.out.println(2 + 2);", MAIN_CLASS_NAME);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleIf() throws Exception {
		MAIN_CLASS_NAME = "testSimpleIf";
		String input = TestHelperJava
				.surroundWithClassMain(
						"boolean b = true; if (b) { System.out.println(2 + 2);} else { System.out.println(2 + 5); }",
						MAIN_CLASS_NAME);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleFor() throws Exception {
		MAIN_CLASS_NAME = "testSimpleFor";
		String input = TestHelperJava
				.surroundWithClassMain(
						"int total = 0; for(int i = 0; i < 4; i++){ total ++;} System.out.println(total);",
						MAIN_CLASS_NAME);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void listAddGet() throws Exception {
		MAIN_CLASS_NAME = "testListAddGet";
		String input = "import java.util.ArrayList;\n"
				+ TestHelperJava
				.surroundWithClassMain(
						"ArrayList<Integer> l = new ArrayList<Integer>(); l.add(4); System.out.println(l.get(0));",
						MAIN_CLASS_NAME);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void mapAddGet() throws Exception {
		MAIN_CLASS_NAME = "testMapAddGet";
		// String input =
		// TestHelper.surroundWithClassMain("HashMap<String, Integer > m = new HashMap<String, Integer>();",
		// "Test");
		String input = "import java.util.HashMap;\n"
				+

								TestHelperJava
								.surroundWithClassMain(
										"String four = \"four\"; HashMap<String, Integer > m = new HashMap<String, Integer>(); m.put(four,4); System.out.println(m.get(four));",
										MAIN_CLASS_NAME);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleNew() throws Exception {
		MAIN_CLASS_NAME = "testSimpleNew";
		String input = TestHelperJava.surroundWithClass(
				"public void printr(){" 
						+ "System.out.println(2 + 2);}"
						+ "public static void main(String[] args){"
						+ MAIN_CLASS_NAME + " t = new "
						+ MAIN_CLASS_NAME + "(); t.printr();}",
						MAIN_CLASS_NAME, ""
				);
		String expected = "4";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleTwoClasses() throws Exception {
		MAIN_CLASS_NAME = "testSimpletwoClasses";
		String input = TestHelperJava.surroundWithClassMain(
				"Printer p = new Printer(); p.printr();", MAIN_CLASS_NAME);
		input += "\n"
				+ TestHelperJava.surroundWithClass(
						"public void printr()  {System.out.println(2 + 2);}",
						"Printer", "");
		String expected = "4";
		//System.out.println(input);
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void simpleForEach() throws Exception {
		MAIN_CLASS_NAME = "testSimpleForEach";
		String input = "import java.util.ArrayList;\n"
				+ TestHelperJava
				.surroundWithClassMain(
						"Integer total = 0;"
								+ " ArrayList<Integer> l = new ArrayList<Integer>();"
								+ " l.add(-2); l.add(-1);l.add(0); l.add(1); l.add(2);l.add(4);"
								+ " for(Integer i : l){"
								+ "total = total + i;" + "}"
								+ "System.out.println(total);",
								MAIN_CLASS_NAME);
		String expected = "4";

		// There is a code generation problem to be fixed for this test in
		// Objective C, so we exclude the ObjC platform for this test.
		// (see GOOL wiki for further documentation)
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void listWithDifferentTypeElement() throws Exception {
		MAIN_CLASS_NAME = "testListWithDifferentTypeElement";
		String input = "import java.util.ArrayList;\n"
				+ TestHelperJava
				.surroundWithClassMain(
						"ArrayList l = new ArrayList();l.add(1);l.add(\"hola\");System.out.println(l.size());",
						MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(input, "2");
	}

	@Test
	public void mapWithoutTypes() throws Exception {
		MAIN_CLASS_NAME = "testMapWithoutTypes";
		String input = "import java.util.HashMap;\n"
				+ TestHelperJava
				.surroundWithClassMain(
						"HashMap m = new HashMap();m.put(0, 1);m.put(\"hola\", 2);System.out.println(m.size());",
						MAIN_CLASS_NAME);

		excludePlatformForThisTest((Platform) CppPlatform.getInstance()); //Generic key type is not supported in C++
		compareResultsDifferentPlatforms(input, "2");
	}

	@Test
	public void removeElementsFromUntypedList() throws Exception {
		MAIN_CLASS_NAME = "testRemoveElementsFromUntypedList";
		String input = "import java.util.ArrayList;\n"
				+ TestHelperJava
				.surroundWithClassMain(
						"ArrayList l = new ArrayList();l.add(\"\");l.add(\"hola\");l.remove(\"hola\");System.out.println(l.size());",
						MAIN_CLASS_NAME);
		excludePlatformForThisTest((Platform) CppPlatform.getInstance()); //No comparison of any typed element available in C++
		compareResultsDifferentPlatforms(input, "1");
	}

	@Test
	public void removeElementsFromIntegerList() throws Exception {
		MAIN_CLASS_NAME = "testRemoveElementsFromIntegerList";
		String input = "import java.util.ArrayList;\n"
				+ TestHelperJava
				.surroundWithClassMain(
						"ArrayList<Integer> l = new ArrayList<Integer>();l.add(1);l.add(4);l.remove(1);System.out.println(l.size());",
						MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(input, "1");
	}

	@Test
	public void removeElementsFromMap() throws Exception {
		MAIN_CLASS_NAME = "testRemoveElementsFromMap";
		String input = "import java.util.HashMap;\n"
				+ TestHelperJava
				.surroundWithClassMain(
						"HashMap<Integer, Integer> m = new HashMap<Integer, Integer>();m.put(1, 2);m.put(2, 3);m.remove(2);System.out.println(m.size());",
						MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(input, "1");
	}

	@Test
	public void isEmptyList() throws Exception {
		MAIN_CLASS_NAME = "testIsEmptyList";
		String input = "import java.util.ArrayList;\n"
				+ TestHelperJava
				.surroundWithClassMain(
						"ArrayList<String> l = new ArrayList<String>();l.add(\"hola\");" +
								"l.remove(\"hola\");" +
								"if(l.isEmpty()){System.out.println(\"1\");}" +
								"else{System.out.println(\"0\");}",
								MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(input, "1");
	}

	@Test
	public void listContainsElement() throws Exception {
		MAIN_CLASS_NAME = "testListContainsElement";
		String input = "import java.util.ArrayList;\n"
				+ TestHelperJava
				.surroundWithClassMain(
						"ArrayList<String> l = new ArrayList<String>();l.add(\"hola\");l.remove(\"hola\");l.add(\"hola\");System.out.println(l.contains(\"hola\"));",
						MAIN_CLASS_NAME);
		compareResultsDifferentPlatforms(new GoolTestExecutor(input, "true",
				platforms, testNotImplementedOnPlatforms) {
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
		//Assert.fail("Not implemented");
	}

	@Test
	public void classWithAttributes() throws Exception {
		MAIN_CLASS_NAME = "testClassWithAttributes";
		String input = "class " + MAIN_CLASS_NAME + "{"
				+ "public int z; public " + MAIN_CLASS_NAME + "(int i){this.z=i+2;}"
				+ "public static void main(String[] args){"
				+ "System.out.println(new " + MAIN_CLASS_NAME + "(5).z);" + "}}";
		compareResultsDifferentPlatforms(input, "7");
	}

	// What is the purpose of the unknownOperator test?
	// -> test xor operator seems ok...
	@Test
	public void unknownOperator() throws Exception {
		MAIN_CLASS_NAME = "testUnknownOperator";
		String input = TestHelperJava.surroundWithClassMain(
				"int total = 1 ^ 0; System.out.println(total);",
				MAIN_CLASS_NAME);
		String expected = "1";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void exceptionThrowTest() throws Exception {
		MAIN_CLASS_NAME = "testExceptionThrowTest";
		String input = TestHelperJava
				.surroundWithClassMain(
						"try {\n " + MAIN_CLASS_NAME + " t=new " + MAIN_CLASS_NAME + "(); t.printr();\n}\n"
								+ "catch(Exception e) {\n System.out.println(\"e\");\n}\n}"
								+ "\n public void printr() throws Exception, Exception {System.out.println(2 + 2);",
								MAIN_CLASS_NAME);
		String expected = "4";
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
