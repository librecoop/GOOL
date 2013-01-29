package gool.generator.python;

import gool.ast.constructs.ClassDef;
import gool.generator.common.CodePrinter;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import logger.Log;

public class PythonCodePrinter extends CodePrinter {
	
	private void createGoolHelperModule(File outputDir) {
		FileOutputStream goolHelperOut;
		byte[] buffer = new byte[1024];
		int noOfBytes;

		// Helpers to create by copying the resource
		List<String> goolHelperIn = new ArrayList<String>();
		goolHelperIn.add("goolHelper/__init__.py");
		goolHelperIn.add("goolHelper/IO.py");
		goolHelperIn.add("goolHelper/Util.py");
		
		// create the directory
		if (! new File(outputDir+"/goolHelper").mkdirs())
			Log.e(String.format("Impossible to create the module '%s/goolHelper'", outputDir));

		// Print helpers
		for(String in : goolHelperIn) {
			InputStream helper;
			try {
				helper = PythonPlatform.class.getResource(in).openStream();

				goolHelperOut = new FileOutputStream (outputDir+"/"+in);
				while ((noOfBytes = helper.read(buffer)) != -1) {
					goolHelperOut.write(buffer, 0, noOfBytes);
				}
				goolHelperOut.close();
				helper.close();
			} catch (IOException e){
				Log.e(e);
			}
		}
		
	}
	
	public PythonCodePrinter(File outputDir) {
		super(new PythonGenerator(), outputDir);
		createGoolHelperModule(outputDir);
	}
	
	@Override
	public String getFileName(String className) {
		return className + ".py";
	}

	@Override
	public String getTemplateDir() {
		return "";
	}

	@Override
	public List<File> print(ClassDef pclass) throws FileNotFoundException {
		List<File> res = super.print(pclass);
		
		createInitFile(getOutputDir());
		
		return res;		
	}
	
	private void createInitFile(File dir) {
		File[] dirs = dir.listFiles(new FileFilter(){
		  public boolean accept(File f) {
		    return f.isDirectory();
		  }
		});
		
		for(File d : dirs) {
			File init = new File(d, "__init__.py");
			try {
				init.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(e);
			}
			createInitFile(d);
		}
	}
}
