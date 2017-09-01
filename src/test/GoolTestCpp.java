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
import gool.generator.objc.ObjcPlatform;
import gool.generator.python.PythonPlatform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GoolTestCpp {

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
//			(Platform) ObjcPlatform.getInstance(Settings.get("objc_out_dir"))

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
			if (excludedPlatforms.contains(platform)) {
				System.err.println("The following target platform(s) have been "
						+ "excluded for this test:" + platform.getName());
				return;
			}

			// This inserts a package which is mandatory for android
			// TODO Not the ideal place to put it also com.test should be in the
			// properties file
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
			String cleanOutput = cleanOutput(TestHelperCpp.generateCompileRun(
					platform, input, MAIN_CLASS_NAME));
			return cleanOutput;
		}

		private static String cleanOutput(String result) {
			return result.replaceAll(CLEAN_UP_REGEX, "").trim();
		}
	}

	private static String MAIN_CLASS_NAME = "TestCpp";

	private List<Platform> testNotImplementedOnPlatforms = new ArrayList<Platform>();

	private void excludePlatformForThisTest(Platform platform) {
		testNotImplementedOnPlatforms.add(platform);
	}

	@BeforeClass
	public static void init() {
	}

	//@Before
	@Test
	public void helloWorld() throws Exception {
		MAIN_CLASS_NAME = "TestCppHelloWorld";
		String input = TestHelperCpp.surroundWithIOSTREAMInclude(TestHelperCpp.surroundWithMain(
				"std::cout << \"Hello World\" << std::endl;"));
		String expected = "Hello World";
		compareResultsDifferentPlatforms(input, expected);
	}
	
//	@Test
//	public void simpleAddition() throws Exception {
//		MAIN_CLASS_NAME = "TestCppSimpleAddition";
//		String input = TestHelperCpp.surroundWithIOSTREAMInclude(TestHelperCpp.surroundWithMain(
//				"int i = 2 + 2 ;"
//				+ "std::cout << i << std::endl;"));
//				
//		String expected = "4";
//		compareResultsDifferentPlatforms(input, expected);
//	}
//
//	@Test
//	public void simpleIf1() throws Exception {
//		MAIN_CLASS_NAME = "TestCppSimpleIf1";
//		String input = TestHelperCpp.surroundWithIOSTREAMInclude(TestHelperCpp.surroundWithMain(
//				"int i = 0 ;"
//				+ "if(i==0) { std::cout << 4 << std::endl; } else { std::cout << 2 << std::endl; }"));
//		String expected = "4";
//		compareResultsDifferentPlatforms(input, expected);
//	}
//
//	@Test
//	public void simpleIf2() throws Exception {
//		MAIN_CLASS_NAME = "TestCppSimpleIf2";
//		String input = TestHelperCpp.surroundWithIOSTREAMInclude(TestHelperCpp.surroundWithMain(
//				"int i = 1 ;"
//				+ "if(i==0) { std::cout << 4 << std::endl; } else { std::cout << 2 << std::endl; }"));
//		String expected = "2";
//		compareResultsDifferentPlatforms(input, expected);
//	}
//	
//	@Test
//	public void simpleFor() throws Exception {
//		MAIN_CLASS_NAME = "TestCppSimpleFor";
//		String input = TestHelperCpp.surroundWithIOSTREAMInclude(TestHelperCpp.surroundWithMain(
//				"int total = 0 ;"
//				+ "for(int i = 0 ; i < 4 ; i++){ total++ ; } "
//				+ " std::cout << total << std::endl; "));
//		String expected = "4";
//		compareResultsDifferentPlatforms(input, expected);
//	}
//
//	@Test
//	public void simpleWhile() throws Exception {
//		MAIN_CLASS_NAME = "TestCppSimpleWhile";
//		String input = TestHelperCpp.surroundWithIOSTREAMInclude(TestHelperCpp.surroundWithMain(
//				"int total = 0 ; int i = 0 ;"
//				+ "while( i < 4){ i++; total++ ; } "
//				+ " std::cout << total << std::endl; "));
//		String expected = "4";
//		compareResultsDifferentPlatforms(input, expected);
//	}
//	
//
//	@Test
//	public void simpleClass() throws Exception {
//		MAIN_CLASS_NAME = "TestCppSimpleClass";
//		String input = "#include <iostream>\n"
//				+ "class A{ public: int k = 5 * 2 + 3 }; "
//		+ TestHelperCpp.surroundWithMain("");
//		String expected = "";
//		compareResultsDifferentPlatforms(input, expected);
//	}
//	
//	@Test
//	public void simpleNew() throws Exception {
//		MAIN_CLASS_NAME = "TestCppSimpleNew";
//		String input = "#include <iostream>\n"
//				+ "class A{ public: "
//				+ " A(int newk){ k = newk; } ; "
//				+ " int k ; }; "
//				+ TestHelperCpp.surroundWithMain(
//						" A a(5*2+3); "
//						+ "int toPrint = a.k ; "
//						+ " std::cout << toPrint << std::endl;"
//						);
//		String expected = "13";
//		
//		// TODO: Fix bugs.
//		//excludePlatformForThisTest((Platform) CppPlatform.getInstance());
//		compareResultsDifferentPlatforms(input, expected);
//	}
//	
//	@Test
//	public void simpleMethod() throws Exception {
//		MAIN_CLASS_NAME = "TestCppSimpleMethod";
//		String input = "#include <iostream>\n"
//				+ "class A{ public: "
//				+ " A(int newk){ k = newk; } ; "
//				+ " int k ; "
//				+ " int getK(){ return k ;} ;  }; "
//				+ TestHelperCpp.surroundWithMain(
//						" A a(5*2+3); "
//						+ "int toPrint = a.getK() ; "
//						+ " std::cout << toPrint << std::endl;"
//						);
//		
//		String expected = "13";	
//		
//		// TODO: Fix bugs.
//		excludePlatformForThisTest((Platform) CppPlatform.getInstance());	
//		compareResultsDifferentPlatforms(input, expected);
//	}
//	
//	@Test
//	public void simpleInheritance1() throws Exception {
//		MAIN_CLASS_NAME = "TestCppSimpleInheritance1";
//		String input = "#include <iostream>\n"
//				+ "class A { public: "
//				+ " int k = 13; "
//				+ " int getK(){ return k ;} ;  }; "
//				+ "class B : public A { public :"
//				+ " B(int newk2){ k2 = newk2 ;} ;"
//				+ " int k2 ; } ;"
//				+ TestHelperCpp.surroundWithMain(
//						" B b(55); "
//						+ "int toPrint = b.getK() ; "
//						+ " std::cout << toPrint << std::endl;"
//						);
//		String expected = "13";
//		
//		// TODO: Fix bugs.
//		excludePlatformForThisTest((Platform) CppPlatform.getInstance());
//		compareResultsDifferentPlatforms(input, expected);
//	}
//	
//	@Test
//	public void simpleInheritance2() throws Exception {
//		MAIN_CLASS_NAME = "TestCppSimpleInheritance2";
//		String input = "#include <iostream>\n"
//				+ "class A { public: "
//				+ " int k = 13; "
//				+ " int getK(){ return k ;} ;  }; "
//				+ "class B : public A { public :"
//				+ " B(int newk2){ k2 = newk2 ;} ;"
//				+ " int k2 ; } ;"
//				+ TestHelperCpp.surroundWithMain(
//						" B b(55); "
//						+ "int toPrint = b.k2 ; "
//						+ " std::cout << toPrint << std::endl;"
//						);
//		String expected = "55";
//		
//		// TODO: Fix bugs.
//		excludePlatformForThisTest((Platform) CppPlatform.getInstance());
//		compareResultsDifferentPlatforms(input, expected);
//	}
//
//	
//	@Test
//	public void simpleFileRefactoring1() throws Exception {
//		MAIN_CLASS_NAME = "TestCppSimpleFileRefactoring1";
//		String input = "#include <iostream>\n"
//				+ "int VARBLOBAL = 4 * 2  - 1; "
//				+ TestHelperCpp.surroundWithMain(
//						 " std::cout << VARBLOBAL << std::endl;"
//						);
//		String expected = "7";
//		
//		// TODO: Fix bugs.
//		excludePlatformForThisTest((Platform) CppPlatform.getInstance());
//		compareResultsDifferentPlatforms(input, expected);
//	}
//	
//	
//	@Test
//	public void simpleFileRefactoring2() throws Exception {
//		MAIN_CLASS_NAME = "TestCppSimpleFileRefactoring2";
//		String input = "#include <iostream>\n"
//				+ "int mult(int s1, int s2){ return s1 * s2 ; } "
//				+ TestHelperCpp.surroundWithMain(
//						" int toPrint = mult(2, 4);"
//						 +" std::cout << toPrint << std::endl;"
//						);
//		String expected = "8";
//		
//		// TODO: Fix bugs.
//		excludePlatformForThisTest((Platform) CppPlatform.getInstance());
//		compareResultsDifferentPlatforms(input, expected);
//	}
//	
//	
//	@Test
//	public void unknownOperator() throws Exception {
//		MAIN_CLASS_NAME = "TestCppUnknownOperator";
//		String input = TestHelperCpp.surroundWithMain(
//				" int toPrint = 5 ^ 4 ; "
//						 +" std::cout << toPrint << std::endl;"
//						);
//		String expected = "";
//		compareResultsDifferentPlatforms(input, expected);
//	}
//	
//	@Test
//	public void externalMethod() throws Exception {
//		MAIN_CLASS_NAME = "TestCppExternalMethod";
//		String input = "#include <iostream>\n"
//				+ "class A { public: "
//				+ " int k ; "
//				+ " A(int newk){ k = newk; } ; "
//				+ " int getK(); }; "
//				+ " int A::getK(){ return k; }"
//				+ TestHelperCpp.surroundWithMain(
//				" A a(55);"
//				+ "int toPrint = a.getK(); ; "
//						 +" std::cout << toPrint << std::endl;"
//						);
//		String expected = "55";
//		
//		// TODO: Fix bugs.
//		excludePlatformForThisTest((Platform) CppPlatform.getInstance());
//		compareResultsDifferentPlatforms(input, expected);
//	}
//	
//	@Test
//	public void externalField() throws Exception {
//		MAIN_CLASS_NAME = "TestCppExternalField";
//		String input = "#include <iostream>\n"
//				+ "class A { public: "
//				+ " int k ; "
//				+ " int k2 = 55 ; "
//				+ " A(int newk){ k = newk; } ; "
//				+ " int getK(){ return k; } ; } ;"
//				+ " A::k2 = 66 ; "
//				+ TestHelperCpp.surroundWithMain(
//				" A a(55);"
//				+ "int toPrint = a.k2; ; "
//						 +" std::cout << toPrint << std::endl;"
//						);
//		String expected = "66";
//		
//		// TODO: Fix bugs.
//		excludePlatformForThisTest((Platform) CppPlatform.getInstance());
//		compareResultsDifferentPlatforms(input, expected);
//	}
//	
//	@Test
//	public void simpleArray() throws Exception {
//		MAIN_CLASS_NAME = "TestCppSimpleArray";
//		String input = "#include <iostream>\n"
//				+ TestHelperCpp.surroundWithMain(
//				" int array[12] ;"
//				+ " array[5] = 158 ; "
//						 +" std::cout <<  array[5] << std::endl;"
//						);
//		String expected = "158";
//		
//		// TODO: Fix bugs.
//		excludePlatformForThisTest((Platform) CppPlatform.getInstance());
//		compareResultsDifferentPlatforms(input, expected);
//	}
//	
//	@Test
//	public void returnValue() throws Exception {
//		MAIN_CLASS_NAME = "TestCppReturnValue";
//		String input = "#include <iostream>\n"
//				+ "int get(){ return 158; }"
//				+ TestHelperCpp.surroundWithMain(
//						 " std::cout <<  get() << std::endl;"
//						);
//		String expected = "158";
//		
//		// TODO: Fix bugs.
//		excludePlatformForThisTest((Platform) CppPlatform.getInstance());
//		compareResultsDifferentPlatforms(input, expected);
//	}
//	
//	@Test
//	public void simpletryCatch() throws Exception {
//		MAIN_CLASS_NAME = "TestCppSimpletryCatch";
//		String input = "#include <iostream>\n"
//				+ TestHelperCpp.surroundWithMain(
//						" int k = 55 ;"
//						+ " try {  } catch (int e){ "
//						 +" std::cout <<  e << std::endl;}"
//						 + "std::cout <<  k << std::endl;"
//						);
//		String expected = "55";
//		
//		// TODO: Fix bugs.
//		excludePlatformForThisTest((Platform) JavaPlatform.getInstance());
//		compareResultsDifferentPlatforms(input, expected);
//	}
//	
//	@Test
//	public void simpleThrow() throws Exception {
//		MAIN_CLASS_NAME = "TestCppSimpleThrow";
//		String input = "#include <iostream>\n"
//				+ TestHelperCpp.surroundWithMain(
//						" try { throw 158; } catch (int e){ "
//						 +" std::cout <<  e << std::endl;}"
//						);
//		String expected = "158";
//		
//		// TODO: Fix bugs.
//		excludePlatformForThisTest((Platform) JavaPlatform.getInstance());
//		compareResultsDifferentPlatforms(input, expected);
//	}
//	
//	@Test
//	public void simpleThis() throws Exception {
//		MAIN_CLASS_NAME = "TestCppSimpleThis";
//		String input = "#include <iostream>\n"
//				+ "class Test { public :"
//				+ "Test(int k){ this.set(k); } ;"
//				+ "int get(){ return this->k ; };"
//				+ "void set(int k){ this->k = k; };"
//				+ "private :"
//				+ "int k ; } ;"
//				+ TestHelperCpp.surroundWithMain(
//						" Test t(55*2); "
//						+ "int toPrint = t.get();"
//						 +" std::cout <<  toPrint << std::endl;}"
//						);
//		String expected = "110";
//		
//		// TODO: Fix bugs.
//		excludePlatformForThisTest((Platform) CppPlatform.getInstance());
//		compareResultsDifferentPlatforms(input, expected);
//	}
//	
//
//	@Test
//	public void simpleRecursive() throws Exception {
//		MAIN_CLASS_NAME = "TestCppSimpleRecursive";
//		String input = "#include <iostream>\n"
//				+ " class A { public :"
//				+ " A(int k){ this.k = k ; } ; "
//				+ " A newA(int k){ A toReturn(k); return toReturn ; };"
//				+ " int getK(){ return this.k ; } ;"
//				+ " int k  ;};"
//				+ "int k ; } ;"
//				+ TestHelperCpp.surroundWithMain(
//						" A a(1); "
//						+ "int toPrint = a.newA(a.k+1).newA(a.k+2).newA(a.k+3).getK();"
//						 +" std::cout <<  toPrint << std::endl;}"
//						);
//		String expected = "4";
//		
//		// TODO: Fix bugs.
//		excludePlatformForThisTest((Platform) CppPlatform.getInstance());
//		compareResultsDifferentPlatforms(input, expected);
//	}
//	
//
//	@Test
//	public void binaryAssign() throws Exception {
//		MAIN_CLASS_NAME = "TestCppBinaryAssign";
//		String input = "#include <iostream>\n"
//				+ TestHelperCpp.surroundWithMain(
//						" int i = 1; "
//						+ "i+=1; i*=2;"
//						 +" std::cout <<  i << std::endl;}"
//						);
//		String expected = "4";
//		compareResultsDifferentPlatforms(input, expected);
//	}
//
//	@Test
//	public void simpleEnum1() throws Exception {
//		MAIN_CLASS_NAME = "TestCppSimpleEnum1";
//		String input = "#include <iostream>\n"
//				+ "enum MyEnum { SMALL = 10, MEDIUM = 100, LARGE = 1000 };"
//				+ TestHelperCpp.surroundWithMain(
//						" MyEnum state = MyEnum.SMALL ; "
//						+ "if(state == MyEnum.SMALL){"
//						+" std::cout <<  \"1\" ;}"
//						+" state = MyEnum.MEDIUM ; "
//						+ "if(state == MyEnum.MEDIUM){"
//						+" std::cout <<  \"2\" ;}"
//						+" state = MyEnum.LARGE ; "
//						+ "if(state == MyEnum.LARGE){"
//						+" std::cout <<  \"3\" ;}"
//						);
//		String expected = "123";
//		
//		// TODO: Fix bugs.
//		excludePlatformForThisTest((Platform) CppPlatform.getInstance());
//		compareResultsDifferentPlatforms(input, expected);
//	}
//
//	@Test
//	public void simpleEnum2() throws Exception {
//		MAIN_CLASS_NAME = "TestCppSimpleEnum2";
//		String input = "#include <iostream>\n"
//				+ "enum MyEnum { SMALL = 10, MEDIUM = 100, LARGE = 1000 };"
//				+ TestHelperCpp.surroundWithMain(
//						" MyEnum state = MyEnum.SMALL ; "
//						+ "if(state == MyEnum.SMALL){"
//						+" std::cout <<  \"1\" ;}"
//						+ "if(state == MyEnum.MEDIUM){"
//						+" std::cout <<  \"2\" ;}"
//						+" state = MyEnum.LARGE ; "
//						+ "if(state == MyEnum.LARGE){"
//						+" std::cout <<  \"3\" ;}"
//						);
//		String expected = "13";
//		
//		// TODO: Fix bugs.
//		excludePlatformForThisTest((Platform) CppPlatform.getInstance());
//		compareResultsDifferentPlatforms(input, expected);
//	}
	
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
