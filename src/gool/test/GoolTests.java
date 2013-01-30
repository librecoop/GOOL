package gool.test;

import gool.GOOLCompiler;
import gool.Settings;
import gool.executor.ExecutorHelper;
import gool.generator.common.Platform;
import gool.generator.java.JavaPlatform;
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

@RunWith(value = Parameterized.class)
public class GoolTests {
	
	//Platforms to be tested
	private static List<Platform> platforms = Arrays.asList(
			JavaPlatform.getInstance(),
//			CSharpPlatform.getInstance(),
//			CppPlatform.getInstance(),
			PythonPlatform.getInstance(new ArrayList<File>()));

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
				File test = new File(dir, "test");
				if(test.exists() && test.isDirectory()) {
					File ini = new File(test_dir + dir.getName() + File.separator + dir.getName() + ".ini");
					if(ini.exists()) {
						tests.put(dir, new Asserts(ini));
					}
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
		deleteDirContent(platform.getCodePrinter().getOutputDir(), false);
		platform.reInitializeCodePrinter();
	}
	
	@Test
	public void test() throws Exception {
		Collection<File> inputFiles = getFilesInFolder(dir, "java");
		
		GOOLCompiler gc = new GOOLCompiler();
		Map<Platform, List<File>> files = gc.concreteJavaToConcretePlatform(platform, inputFiles);

		String result = ExecutorHelper.compileAndRun(platform, files);
		
		//Check the output
		Assert.assertEquals(name + ": output", asserts.getOutput(), result);
		
		//Check folders presents
		checkFolders();
		
		//Check files presents and ok
		checkFiles();
	}
	
	private void checkFolders() {
		List<String> dirs = asserts.getDirectories();
		File folder;
		
		for(String name : dirs) {
			folder = new File(platform.getCodePrinter().getOutputDir(), name);
			Assert.assertEquals(this.name + ": folder " + name, true, folder.exists() && folder.isDirectory());
		}
	}
	
	private void checkFiles() {
		Map<String, String> fs = asserts.getFiles();
		File file;
		
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
	
	private String output = "";
	private Map<String, String> files = new HashMap<String, String>();
	private List<String> directories = new ArrayList<String>();
	
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