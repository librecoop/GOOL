package gool.executor.python;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSourceCodeExtension() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String run(File file, List<File> classPath)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File compileToObjectFile(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
