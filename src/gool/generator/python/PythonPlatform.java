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

import logger.Log;

public class PythonPlatform extends Platform {

	private final String outputDir = Settings.get("python_out_dir");
	
	protected PythonPlatform() {
		super("PYTHON");
		// create goolHelper.py by copying the resource
		// TODO: do not work if outputDir dosen't exist
		FileOutputStream goolHelperOut;
		InputStream goolHelperIn;
		byte[] buffer = new byte[1024];
		int noOfBytes;
		goolHelperIn = PythonPlatform.class.getResourceAsStream("goolHelper.py");
		File dir = new File(outputDir);
		if (! dir.exists()) {
			dir.mkdirs();
		}
		try {
			goolHelperOut = new FileOutputStream (outputDir+"/goolHelper.py");
			while ((noOfBytes = goolHelperIn.read(buffer)) != -1)
				goolHelperOut.write(buffer, 0, noOfBytes);
			goolHelperOut.close();
			goolHelperIn.close();
		} catch (IOException e){
			Log.e(e);
			System.exit(1);
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
