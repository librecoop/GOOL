package gool.executor.csharp;

import gool.Settings;
import gool.executor.Command;
import gool.executor.common.SpecificCompiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logger.Log;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CSharpCompiler extends SpecificCompiler {
	private static Logger logger = Logger.getLogger(CSharpCompiler.class.getName());
	private static final boolean IS_WINDOWS = System.getProperty("os.name")
			.toUpperCase().contains("WINDOWS");

	public CSharpCompiler(File outputDir, List<File> deps) {
		super(outputDir, deps);
	}

	@Override
	public File compileToExecutable(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
			throws FileNotFoundException {
		List<String> params = new ArrayList<String>();

		if (mainFile == null) {
			Log.i(files.toString());
			mainFile = files.get(0);
		}

			String execFileName = mainFile.getName().replace(".cs", ".exe");
			params.addAll(Arrays.asList(Settings.get("csharp_compiler_cmd"), "-debug+", "/t:exe",
					"/out:" + execFileName));
		
		/*
		 * Add the needed dependencies to be able to compile programs.
		 */
		if (classPath != null) {
			for (File dependency : classPath) {
				params.add("/r:" + dependency.getAbsolutePath());
			}
		}

		for (File dependency : getDependencies()) {
			params.add("/r:" + dependency.getAbsolutePath());
		}

		for (File file : files) {
			params.add(file.toString());
		}
		Command.exec(getOutputDir(), params);
		return new File(getOutputDir(), execFileName);
	}
	
	
	@Override
	public File compileToObjectFile(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
			throws FileNotFoundException {
		// TODO Duplicate code in compile and compileAll
		
		if (mainFile == null) {
			Log.i(files.toString());
			mainFile = files.get(0);
		}
		
		String execFileName = mainFile.getName().replace(".cs", ".dll");

		List<String> params = new ArrayList<String>();
		params.addAll(Arrays.asList(Settings.get("csharp_compiler_cmd"), "-debug+", "/t:library"));
		

		/*
		 * Add the needed dependencies to be able to compile programs.
		 */
		if (classPath != null) {
			for (File dependency : classPath) {
				params.add("/r:" + dependency.getAbsolutePath());
			}
		}

		for (File dependency : getDependencies()) {
			params.add("/r:" + dependency.getAbsolutePath());
		}

		for (File file : files) {
			params.add(file.toString());
		}
		Command.exec(getOutputDir(), params);
		return new File(getOutputDir(), execFileName);
	}

	@Override
	public String run(File file, List<File> classPath)
			throws FileNotFoundException {
		String[] runTest = IS_WINDOWS ? new String[] { new File(getOutputDir(),	file.getName()).getAbsolutePath() }
				: new String[] { "mono", file.getName() };
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
		env.put("MONO_PATH", StringUtils.join(deps, File.separator));

		params.addAll(Arrays.asList(runTest));
		return Command.exec(getOutputDir(), params, env);
	}

	@Override
	public String getSourceCodeExtension() {
		return "cs";
	}


}
