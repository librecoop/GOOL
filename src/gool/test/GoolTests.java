package gool.test;

import gool.GOOLCompiler;

import gool.Settings;
import gool.executor.ExecutorHelper;
import gool.generator.common.Platform;
import gool.generator.java.JavaPlatform;
import gool.generator.python.PythonPlatform;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logger.Log;

import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class GoolTests {
	
	//Platforms to be tested
	private static List<Platform> platforms = Arrays.asList((Platform)JavaPlatform.getInstance(),/*CSharpPlatform.getInstance(),  CppPlatform.getInstance(),*/ PythonPlatform.getInstance());

	private String name;	//Name of the test
	private Platform platform;	//Platform for the test
	private File dir;	//Folder for the test
	private Asserts asserts;	//Asserts object containing information on the result expected


	public GoolTests(String name, Platform p, File dir, Asserts a) {
		this.name = name;
		this.dir = dir;
		asserts = a;
		platform = p;
	}
	
	//Create the list of tests
	@Parameters(name="{0}") //Available to properly name tests in JUnit 4.11
	public static Collection<Object[]> data() {
		Map<File, Asserts> tests = new HashMap<File, Asserts>();
		String test_dir = Settings.get("test_dir");	//Directory containing the tests
		File f = new File(test_dir);
		File[] dirs = f.listFiles(new FileFilter() { public boolean accept(File f) {
			return f.isDirectory();
		}});
		
		//If there are directories of tests
		if(dirs != null) {
			//For each test folder, we parse its ini file
			for(File dir : dirs) {
				File ini = new File(test_dir + dir.getName() + ".ini");
				if(ini.exists()) {
					tests.put(dir, new Asserts(ini));
				}
			}
	
			Object[][] data = new Object[tests.size()*platforms.size()][];
				
			int i=0;
			for(Map.Entry<File, Asserts> test : tests.entrySet()) {
				for(Platform platform : platforms) {
					data[i++] = new Object[] { test.getKey().getName() + " / " + platform.getName(), platform, test.getKey(), test.getValue() };
				}
			}
			
			return Arrays.asList(data);
		}
		return new ArrayList<Object[]>();
	}
	
	@Before
	public void before() {
		for(Platform platform : platforms) {
			deleteDirContent(platform.getCodePrinter().getOutputDir(), false);
		}
		platform.reInitializeCodePrinter();
	}
	
	@Test
	public void test() throws Exception {
		Collection<File> inputFiles = getFilesInFolder(dir, "java");
		
		GOOLCompiler gc = new GOOLCompiler();
		Map<Platform, List<File>> files = gc.concreteJavaToConcretePlatform(platform, inputFiles);

		String result = ExecutorHelper.compileAndRun(platform, files);
		Assert.assertEquals(name, asserts.getOutput(), result);
	}
	
	//Clean up output directories
	private void deleteDirContent(File dir, boolean delete) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				deleteDirContent(new File(dir, children[i]), true);
			}
		}
		if(delete) {
			dir.delete();
		}
	} 

	//Files in folder
	private static Collection<File> getFilesInFolder(File folder, String ext) {
		Collection<File> files = new ArrayList<File>();
		for(File f : folder.listFiles()) {
			if(f.isDirectory()) {
				files.addAll(getFilesInFolder(f, ext));
			}
			else if ( f.getName().endsWith(ext)) {
				files.add(f);
			}
		}
		return files;
	}
}

class Asserts {
	
	private String output;
	
	public Asserts (File fIni) {
		Ini ini = new Ini();
		Config cfg = new Config();
		cfg.setMultiSection(true);
		ini.setConfig(cfg);
		try {
			ini.load(fIni);
			
			//Core configs
			Ini.Section core = ini.get("core");
			output = core.get("output", String.class);
			if(output == null) {
				output = "";
			} else {
				output = output.substring(1, output.length()-1);
			}
		} catch (InvalidFileFormatException e) {
			Log.e(e);
		} catch (IOException e) {
			Log.e(e);
		}
	}
	
	public String getOutput() {
		return output;
	}
}