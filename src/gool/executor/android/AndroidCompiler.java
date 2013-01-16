package gool.executor.android;

import gool.Settings;
import gool.executor.Command;
import gool.executor.CommandException;
import gool.executor.common.SpecificCompiler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

public class AndroidCompiler extends SpecificCompiler {

	public AndroidCompiler(File androidOutputDir, List<File> deps) {
		super(androidOutputDir, deps);
	}

	@Override
	public File compileToExecutable(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
			throws FileNotFoundException {
		List<String> params = new ArrayList<String>();
		params.add("android");
		params.add("create project ");
		params.add("--target 1 ");
		params.add("--name Bazinga ");
		params.add("--path ./MyAndroidAppProject ");
		params.add("--activity MyAndroidAppActivity ");
		params.add("--package com.bazinga.android ");
		addSdkToClasspath();
//		if (mainFile == null) {
//			mainFile = files.get(0);
//		}
//		for (File file : files) {
//			params.add(file.toString());
//		}
//		if (args != null) {
//			params.addAll(args);
//		}
		Command.exec(getOutputDir(), params);	
		return (new File(getOutputDir(), mainFile.getName().replace(".java", ".class")));
	}
	
	@Override
	public File compileToObjectFile(List<File> files,  File mainFile,
			List<File> classPath, List<String> args) throws FileNotFoundException {
		return compileToExecutable(files, mainFile, classPath, args);
	}
	

	@Override
	public String run(File file, List<File> classPath)
			throws FileNotFoundException {
		List<String> params = new ArrayList<String>();
		params.add("java");

		addSdkToClasspath();

		params.add(file.getName().replace(".class", ""));
		return Command.exec(getOutputDir(), params);
	}

	private void addSdkToClasspath(){
		/*
		 * Add the classpath
		 */
		try {
			
			String addSdkDir="export PATH=$PATH:"+Settings.get("android_sdk_location")+"/tools:"+Settings.get("android_sdk_location")+"/platform-tools";
			addSdkDir.concat(Settings.get("android_sdk_location")+"/platform-tools");
			FileWriter fstream = new FileWriter("android.sh");
	        BufferedWriter out = new BufferedWriter(fstream);
	        
		    out.write(addSdkDir);
		    out.newLine();
		    out.write("android create project  --target 1  --name Bazinga  --path ./MyAndroidAppProject  --activity MyAndroidAppActivity  --package com.bazinga.android");
		    out.close();
		    Runtime.getRuntime().exec("sh android.sh");
		    Runtime.getRuntime().exec("ant debug");
//		    Runtime.getRuntime().exec("android create project  --target 1  --name Bazinga  --path ./MyAndroidAppProject  --activity MyAndroidAppActivity  --package com.bazinga.android");
//			ProcessBuilder pb = new ProcessBuilder("android.sh");		
//			Map<String,String> env=pb.environment();
//			Process p = pb.redirectErrorStream(true).start();
//			p.getOutputStream().close();
//			BufferedReader in = new BufferedReader(new InputStreamReader(p
//					.getInputStream()));
//			StringBuffer buffer = new StringBuffer();
//			String line;
//			while ((line = in.readLine()) != null) {
//				buffer.append(line).append("\n");
//			}
//	
//			int retval = p.waitFor();
//	
//			if (retval != 0) {
//				throw new CommandException("The command execution returned "
//						+ retval + " as return value... !\n" + buffer);
//			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		catch (InterruptedException e) {
//			throw new CommandException("It seems the process was killed", e);
//		}
	}

	@Override
	public String getSourceCodeExtension() {
		return "java";
	}
}
