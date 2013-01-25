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
import java.util.HashMap;
import java.util.List;

import logger.Log;

public class PythonPlatform extends Platform {

	private final String outputDir = Settings.get("python_out_dir");
	
	protected PythonPlatform() {
		super("PYTHON");
		
		// create goolHelper.py by copying the resource
		FileOutputStream goolHelperOut;
		byte[] buffer = new byte[1024];

		List<String> goolHelperIn = new ArrayList<String>();
		int noOfBytes;
		
		//Helpers to create
		goolHelperIn.add("goolHelper.py");
		goolHelperIn.add("goolHelperIO.py");
		
		//Test output folder exists
		File folder = new File(outputDir);
		if(!folder.exists()) {
			folder.mkdir();
		}
		
		//Print helpers
		for(String in : goolHelperIn) {
			InputStream helper = PythonPlatform.class.getResourceAsStream(in);
			try {
				goolHelperOut = new FileOutputStream (outputDir+"/"+in);
				while ((noOfBytes = helper.read(buffer)) != -1) {
					goolHelperOut.write(buffer, 0, noOfBytes);
				}
				goolHelperOut.close();
				helper.close();
			} catch (IOException e){
				Log.e(e);
				System.exit(1);
			}
		}
	}

	@Override
	protected CodePrinter initializeCodeWriter() {
		return new PythonCodePrinter(new File(outputDir));
	}

	@Override
	protected SpecificCompiler initializeCompiler() {
		return new PythonCompiler(new File(outputDir), new ArrayList<File>());
	}

	private static PythonPlatform instance = new PythonPlatform();

	public static PythonPlatform getInstance() {
		return instance;
	}
	public static void newInstance() {
		instance = new PythonPlatform();
	}
	
}
