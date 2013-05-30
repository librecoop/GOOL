package gool.test;

import gool.GOOLCompiler;
import gool.Settings;
import gool.executor.ExecutorHelper;
import gool.generator.android.AndroidPlatform;
import gool.generator.common.Platform;
import gool.generator.cpp.CppPlatform;
import gool.generator.csharp.CSharpPlatform;
import gool.generator.java.JavaPlatform;
import gool.generator.objc.ObjcPlatform;
import gool.generator.python.PythonPlatform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

// TODO: Auto-generated Javadoc
/**
 * The Class GoolTests, for unit testing Gool.
 * Tests are in the folder tests in the root folder of Gool.
 * Each test must be in a folder named with the name of the test. In this folder a file .ini named with the
 * name of the parent folder contains asserts for the test, and a folder "test" contain the program to test.
 */
@RunWith(value = Parameterized.class)
public class GoolTestBattery {
	
	/** The platforms to test. */
	private static List<Platform> platforms = Arrays.asList(
//			(Platform)JavaPlatform.getInstance()
			(Platform)CSharpPlatform.getInstance()
//			(Platform)CppPlatform.getInstance()
//			(Platform)PythonPlatform.getInstance()
//			(Platform)AndroidPlatform.getInstance(),
//			(Platform)ObjcPlatform.getInstance()
			);
	
	/** The name of the test. */
	private String name;
	
	/** The platform of the test. */
	private Platform platform;
	
	/** The input files. */
	private Collection<File> inputFiles;
	
	/** The asserts. */
	private Asserts asserts;


	/**
	 * Instantiates a new gool tests.
	 *
	 * @param name the name
	 * @param p the platform
	 * @param inputFiles the input files
	 * @param a the aasserts
	 */
	public GoolTestBattery(String name, Platform p, Collection<File> inputFiles, Asserts a) {
		this.name = name;
		this.inputFiles = inputFiles;
		asserts = a;
		platform = p;
	}
	
	/**
	 * Create tests.
	 *
	 * @return the collection
	 */
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
				File test = new File(dir, "test");
				if(test.exists() && test.isDirectory()) {
					File ini = new File(dir.getAbsolutePath() + File.separator + dir.getName() + ".ini");
					
					if(ini.exists()) {
						tests.put(dir, new Asserts(ini));
					}
				}
			}
	
			Object[][] data = new Object[tests.size()*platforms.size()][];
				
			//Init the object containing the tests
			int i=0;
			for(Map.Entry<File, Asserts> test : tests.entrySet()) {
				Collection<File> inputFiles = GOOLCompiler.getFilesInFolder(test.getKey(), "java");
				for(Platform platform : platforms) {
					data[i++] = new Object[] { test.getKey().getName() + " / " + platform.getName(), platform, inputFiles, test.getValue() };
				}
			}
			
			return Arrays.asList(data);
		}
		return new ArrayList<Object[]>();
	}
	
	/**
	 * Before.
	 */
	@Before
	public void before() {
		deleteDirContent(platform.getCodePrinter().getOutputDir(), false);	//Clean up the output directory
		platform.reInitializeCodePrinter();
	}
	
	/**
	 * Test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void test() throws Exception {		
		Map<Platform, List<File>> files = GOOLCompiler.concreteJavaToConcretePlatform(platform, inputFiles);

		String result = ExecutorHelper.compileAndRun(platform, files);
		
		
		//The following instruction is used to remove some logging data
		//at the beginning of the result string
		if(platform == ObjcPlatform.getInstance() && result.indexOf("] ")!=-1)
			result=result.substring(result.indexOf("] ")+2);
		
		//Check the output
		Assert.assertEquals(name + ": output", asserts.getOutput(), result);
		
		//Check folders presents
		checkFolders();
		
		//Check files presents and ok
		checkFiles();
	}
	
	/**
	 * Check folders.
	 */
	private void checkFolders() {
		List<String> dirs = asserts.getDirectories();
		File folder;
		
		for(String name : dirs) {
			folder = new File(platform.getCodePrinter().getOutputDir(), name);
			Assert.assertEquals(this.name + ": folder " + name, true, folder.exists() && folder.isDirectory());
		}
	}
	
	/**
	 * Check files.
	 */
	private void checkFiles() {
		Map<String, String> fs = asserts.getFiles();
		File file;
		
		//For each file that must be checked we look if they exist and have the content expected
		for(Map.Entry<String, String> f : fs.entrySet()) {
			file = new File(platform.getCodePrinter().getOutputDir(), f.getKey());
			Assert.assertEquals(name + ": file " + f.getKey(), true, file.exists() && file.isFile());
			
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				StringBuilder sb = new StringBuilder();
		        int c = br.read();
				
				while(c != -1) {
					sb.append((char)c);
					c = br.read();
				}
				br.close();
				
				Assert.assertEquals(name + ": file content " + f.getKey(), f.getValue(), sb.toString());
			} catch (FileNotFoundException e) {
				Log.e(e);
			} catch (IOException e) {
				Log.e(e);
			}
			
		}
	}
	
	/**
	 * Clean up the output directory.
	 *
	 * @param dir the directory to clean
	 * @param delete the delete
	 */
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
}

class Asserts {
	
	private String output = ""; //output expected
	private Map<String, String> files = new HashMap<String, String>();	//files to be ckecked
	private List<String> directories = new ArrayList<String>(); //directories to be checked
		
	public Asserts (File fIni) {
		Ini ini = new Ini();
		Config cfg = new Config();
		cfg.setMultiSection(true);
		ini.setConfig(cfg);
		try {
			ini.load(fIni);
			
			//Core configs
			Ini.Section core = ini.get("core");
			if(core != null) {
				output = core.get("output", String.class);
				if(output == null) {
					output = "";
				} else {
					output = output.substring(1, output.length()-1);
				}
			}
			
						
			//Directories to be checked
			List<Ini.Section> dirs = ini.getAll("directory");
			if(dirs != null) {
				for(Ini.Section dir : dirs) {
					try {
						directories.add(dir.get("name", String.class));
					} catch (Exception e) {
					}
				}
			}
			
			//Files to be checked
			List<Ini.Section> fs = ini.getAll("file");
			if(fs != null) {
				for(Ini.Section f : fs) {
					try {
						String name = f.get("name", String.class);
						
						if(name != null) {
							String content = f.get("content", String.class);
							if(content == null) {
								content = "";
							} else {
								content = content.substring(1, content.length()-1);
							}
							
							files.put(name, content);
						}
					} catch (Exception e) {
					}
				}
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

	public Map<String, String> getFiles() {
		return files;
	}

	public List<String> getDirectories() {
		return directories;
	}
}