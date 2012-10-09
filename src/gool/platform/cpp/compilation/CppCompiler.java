package gool.platform.cpp.compilation;

import gool.platform.common.SpecificCompiler;
import gool.util.compiler.Command;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CppCompiler extends SpecificCompiler {

	private static final boolean IS_WINDOWS = System.getProperty("os.name")
			.toUpperCase().contains("WINDOWS");

	public CppCompiler(File outputDir, List<File> deps) {
		super(outputDir, deps);
	}

	@Override
	public File compileToExecutable(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
			throws FileNotFoundException {
		List<String> params = new ArrayList<String>();
		if (mainFile == null) {
			mainFile = files.get(0);
		}
		System.out.println("--->" + mainFile);
		String execFileName = mainFile.getName().replace(".cpp",".bin");
		params.addAll(Arrays.asList("g++", "-I","/usr/include", "-o", execFileName) );

		/*
		 * Add the needed dependencies to be able to compile programs.
		 */
		if (classPath != null) {
			for (File dependency : classPath) {
				params.add(dependency.getAbsolutePath());
			}
		}

		for (File dependency : getDependencies()) {
			params.add(dependency.getAbsolutePath());
		}

		for (File file : files) {
			params.add(file.toString());
		} 

		Command.exec(getOutputDir(), params);
		return new File(getOutputDir(), execFileName);
	}
	
	@Override
	public File compileToObjectFile(List<File> files, File mainFile,
			List<File> classPath, List<String> args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String run(File file, List<File> classPath)
			throws FileNotFoundException {
		List<String> params = new ArrayList<String>();

		List<String> deps = new ArrayList<String>();
		if (classPath != null) {
			for (File dependency : classPath) {
				deps.add(dependency.getParent());
			}
		}
		for (File dependency : getDependencies()) {
			deps.add(dependency.getParent());
		}

		Map<String, String> env = new HashMap<String, String>();

		params.addAll(Arrays.asList("./"+file.getName()));
		return Command.exec(getOutputDir(), params, env);
	}

	@Override
	public String getSourceCodeExtension() {
		return "cs";
	}
}