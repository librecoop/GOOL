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
import logger.Log;
import gool.generator.objc.ObjcPlatform;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GoolTestIsPrimary {

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
			if (excludedPlatforms.contains(platform)){
				System.err.println("The following target platform(s) have been "
						+ "excluded for this test:" + platform.getName());
				return;
			}

			String expect = this.expected;
			if (platform instanceof CppPlatform){// C++ does not have booleans
				expect = expect.toLowerCase().replaceAll("true", "1");
				expect = expect.toLowerCase().replaceAll("false", "0");
			}
		
			String result = compileAndRun(platform);
			// The following instruction is used to remove some logging data
			// at the beginning of the result string
			if (platform == ObjcPlatform.getInstance()
					&& result.indexOf("] ") != -1)
				result = result.substring(result.indexOf("] ") + 2);

			Assert.assertEquals(String.format("The platform %s", platform),
					expect.toLowerCase(), result.toLowerCase());
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

	private static final String MAIN_CLASS_NAME = "TestIsPrimary";

	private List<Platform> testNotImplementedOnPlatforms = new ArrayList<Platform>();

	private void excludePlatformForThisTest(Platform platform) {
		testNotImplementedOnPlatforms.add(platform);
	}


	@BeforeClass
	public static void init() {
	}

	@Test
	public void isPrimaryIntTest() throws Exception {
		String input = TestHelperJava.surroundWithClass(
				"public boolean isPrimary(int nombre){"
						+ "for (int i=2; i<nombre; i++){"
						+ "if (nombre%i == 0){return false;}"
						+ "}"
						+ "return true;}"
						+ "public static void main(String[] args){"
						+ MAIN_CLASS_NAME + " test = new " + MAIN_CLASS_NAME + "();"
						+ "System.out.println(test.isPrimary(7));"
						+ "System.out.println(test.isPrimary(4));}"
						,MAIN_CLASS_NAME,"");

		String expected = "true"+"false";
		compareResultsDifferentPlatforms(input, expected);
	}

	@Test
	public void isPrimaryDoubleTest() throws Exception {
		String input = TestHelperJava.surroundWithClass(
				"public boolean isPrimary(double nombre){"
						+ "for (double i=2.0; i<nombre; i++){"
						+ "if ((nombre%i) == 0.0){return false;}"
						+ "}"
						+ "return true;}"
						+ "public static void main(String[] args){"
						+ MAIN_CLASS_NAME + " test = new " + MAIN_CLASS_NAME + "();"
						+ "System.out.println(test.isPrimary(7.0));"
						+ "System.out.println(test.isPrimary(4.0));}"
						,MAIN_CLASS_NAME,"");

		String expected = "true"+"false";
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
