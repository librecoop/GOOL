package gool.executor.python;

import gool.executor.Command;
import gool.executor.common.SpecificCompiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logger.Log;

public class PythonCompiler extends SpecificCompiler {

	public PythonCompiler(File pythonOutputDir, ArrayList<File> arrayList) {
		super(pythonOutputDir, arrayList);
	}

	@Override
	public File compileToExecutable(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
			throws FileNotFoundException {
		return files.get(0);
	}

	@Override
	public String getSourceCodeExtension() {
		return "py";
	}

	@Override
	public String run(File file, List<File> classPath)
			throws FileNotFoundException {
		
		Map<String, String> env = new HashMap<String, String>();
		env.put("PYTHONPATH", getOutputDir().getAbsolutePath());
		
		List<String> params = new ArrayList<String>();
		params.add("python");
		params.add(file.getAbsolutePath());
		return Command.exec(getOutputDir(), params, env);
	}

	@Override
	public File compileToObjectFile(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
			throws FileNotFoundException {
		return mainFile;
	}
}
