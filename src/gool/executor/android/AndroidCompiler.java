package gool.executor.android;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import gool.executor.common.SpecificCompiler;

public class AndroidCompiler extends SpecificCompiler{

	public AndroidCompiler(File dir, List<File> deps) {
		super(dir, deps);
		// TODO Auto-generated constructor stub
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
