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
import gool.generator.android.AndroidPlatform;
import gool.generator.common.Platform;
import gool.generator.cpp.CppPlatform;
import gool.generator.csharp.CSharpPlatform;
import gool.generator.java.JavaPlatform;
import gool.generator.python.PythonPlatform;
import gool.generator.objc.ObjcPlatform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HeritageTest {

	/*
	 * At this day, the GOOL system supports 6 output languages that are
	 * symbolized by Platforms. You may comment/uncomment these platforms to
	 * enable/disable tests for the corresponding output language.
	 * 
	 * You may also add your own tests by creating a new method within this
	 * class preceded by a @Test annotation.
	 */
	private List<Platform> platforms = Arrays.asList(

			//(Platform) JavaPlatform.getInstance(),
			(Platform) CSharpPlatform.getInstance(),
		   (Platform) CppPlatform.getInstance(),
			(Platform) PythonPlatform.getInstance()// ,
//			 (Platform) AndroidPlatform.getInstance() ,
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

		public void compare(Platform platform) throws Exception {
			if (excludedPlatforms.contains(platform)) {
				String errorMsg = "The following target platform(s) have been excluded for this test: ";
				for (Platform p : excludedPlatforms)
					if (testedPlatforms.contains(p))
						errorMsg += p + " ";
				Assert.fail(errorMsg
						+ "\nThis test may contain some patterns that are not supported by GOOL at the moment for these target platforms. You may see the GOOL wiki for further documentation.");
			}

			// This inserts a package which is mandatory for android
			// TODO Not the ideal place to put it also com.test should be in the
			// properties file
			if (platform instanceof AndroidPlatform) {
				this.input = "package com.test; " + input;
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

	private static final String MAIN_CLASS_NAME = "Test";

	private List<Platform> testNotImplementedOnPlatforms = new ArrayList<Platform>();

	private void excludePlatformForThisTest(Platform platform) {
		testNotImplementedOnPlatforms.add(platform);
	}

	@BeforeClass
	public static void init() {
	}

	@Before
	@Test
	public void HeritageTest() throws Exception {
		String input = "public class A {  "+
		     "protected int i; " + 
		     "protected int j;" + 
		     "public A(){"+
		     "i=10; j=30;}"+
		     " public  void uneMethode() {" + 
		        "System.out.println(i) ;}"+
			    "public  static  void main(String args[]){"+
		    	"A a =  new A();"  + 
		    	"B b =  new B();" +   
		    	"a.uneMethode() ;"+
	            "b.uneAutreMethode();" +
	            "}"+
		      "}"+
		 " class B extends A {"  + 
		     "protected int i;"+ 
		     "public B(){"+
		     "i=20;}"+
		     "public  void uneAutreMethode() {"+  
		        "System.out.println(i) ;"+
		        "System.out.println(j);"+  
		    "}" +
		    "}"
		   ;
		
			String expected="10"+"20"+"30";
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


