package gool.generator.python;

import gool.ast.constructs.ClassDef;
import gool.generator.common.CodePrinter;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import logger.Log;

public class PythonCodePrinter extends CodePrinter {
	
	public PythonCodePrinter(File outputDir) {
		super(new PythonGenerator(), outputDir);
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
		
		File rep = getOutputDir();
		File[] dirs = rep.listFiles(new FileFilter(){
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
		}
		
		return res;		
	}
}
