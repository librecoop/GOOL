package gool.executor.python;

import gool.executor.Command;
import gool.executor.common.SpecificCompiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
		List<String> params = new ArrayList<String>();
		params.add("python");
		
		params.add(file.getName());
		return Command.exec(getOutputDir(), params);
	}

	@Override
	public File compileToObjectFile(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
			throws FileNotFoundException {
		return mainFile;
	}
}
