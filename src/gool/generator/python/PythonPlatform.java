package gool.generator.python;

import gool.Settings;
import gool.executor.common.SpecificCompiler;
import gool.executor.python.PythonCompiler;
import gool.generator.common.CodePrinter;
import gool.generator.common.Platform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import logger.Log;

public class PythonPlatform extends Platform {

	private final String outputDir = Settings.get("python_out_dir");
	
	protected PythonPlatform(Collection<File> myFile) {
		
		
		
		super("PYTHON", myFile);

		
		// create goolHelper.py by copying the resource
		FileOutputStream goolHelperOut;
		byte[] buffer = new byte[1024];

		List<String> goolHelperIn = new ArrayList<String>();
		int noOfBytes;
		
		//Helpers to create
		goolHelperIn.add("goolHelper.py");
		goolHelperIn.add("goolHelperIO.py");
		goolHelperIn.add("goolHelperUtil.py");
		
		//Test output folder exists
		File folder = new File(outputDir);
		if(!folder.exists()) {
			folder.mkdir();
		}
		
		//Print helpers
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

	@Override
	protected CodePrinter initializeCodeWriter() {
		return new PythonCodePrinter(new File(outputDir), myFileToCopy);
	}

	@Override
	protected SpecificCompiler initializeCompiler() {
		return new PythonCompiler(new File(outputDir), new ArrayList<File>());
	}

	private static PythonPlatform instance = new PythonPlatform(myFileToCopy);

	public static PythonPlatform getInstance(Collection<File> myF) {
		myFileToCopy = myF;
		return instance;
	}
	public static void newInstance() {
		instance = new PythonPlatform(myFileToCopy);
	}
	
}
