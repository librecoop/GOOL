package gool.executor.android;

import gool.Settings;
import gool.executor.Command;
import gool.executor.CommandException;
import gool.executor.common.SpecificCompiler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AndroidCompiler extends SpecificCompiler {

	public AndroidCompiler(File androidOutputDir, List<File> deps)
	{
		super(androidOutputDir, deps);
	}

	@Override
	public File compileToExecutable(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
			throws FileNotFoundException {
		List<String> params = new ArrayList<String>();
		params.add("ant");
		params.add("debug");
//		addSdkToClasspath();
//		if (mainFile == null) {
//			mainFile = files.get(0);
//		}
//		for (File file : files) {
//			params.add(file.toString());
//		}
//		if (args != null) {
//			params.addAll(args);
//		}
		Command.exec(new File(Settings.get("android_out_dir_final")), params);	
		return (new File(Settings.get("android_out_dir_final")));
	}
	
	@Override
	public File compileToObjectFile(List<File> files,  File mainFile,
			List<File> classPath, List<String> args) throws FileNotFoundException {
		return compileToExecutable(files, mainFile, classPath, args);
	}
	

	@Override
	public String run(File file, List<File> classPath)
			throws FileNotFoundException {
		try{
			
			List<String> paramsToRunEmulator = new ArrayList<String>();
			ProcessBuilder pbForEmulator=new ProcessBuilder(paramsToRunEmulator);
			paramsToRunEmulator.add("android");
			paramsToRunEmulator.add("avd");
			pbForEmulator.directory(new File(Settings.get("android_sdk_path")));
			Process avdManagerStart = pbForEmulator.redirectErrorStream(true).start();
			int retval = avdManagerStart.waitFor();
				if (retval != 0)
				{
						throw new CommandException("The command execution returned "
								+ retval + " as return value... !\n");
				}
			return installApkOnPhone();
		}
		catch (IOException e) {
			throw new CommandException(e);
		} catch (InterruptedException e) {
			throw new CommandException("It seems the process was killed", e);
		}
		//addSdkToClasspath();
		
	}

//	private void addSdkToClasspath(){
//		/*
//		 * Add the classpath
//		 */
//		try {
//			
//			String addSdkDir="export PATH=$PATH:"+Settings.get("android_sdk_location")+"/tools:"+Settings.get("android_sdk_location")+"/platform-tools";
//			addSdkDir.concat(Settings.get("android_sdk_location")+"/platform-tools");
//			FileWriter fstream = new FileWriter("android.sh");
//	        BufferedWriter out = new BufferedWriter(fstream);
//	        
//		    out.write(addSdkDir);
//		    out.newLine();
//		    out.write("android create project  --target 1  --name Bazinga  --path ./MyAndroidAppProject  --activity MyAndroidAppActivity  --package com.bazinga.android");
//		    out.close();
//		    Runtime.getRuntime().exec("sh android.sh");
//		    Runtime.getRuntime().exec("ant debug");
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
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		catch (InterruptedException e) {
//			throw new CommandException("It seems the process was killed", e);
//		}
//	}

	@Override
	public String getSourceCodeExtension() {
		// TODO Auto-generated method stub
		return null;
	}
	
	String installApkOnPhone()
	{
		try{
			
			List<String> paramsToInstallApk = new ArrayList<String>();
			ProcessBuilder pbForApk=new ProcessBuilder(paramsToInstallApk);
			paramsToInstallApk.add("adb");
			paramsToInstallApk.add("install");
			paramsToInstallApk.add(Settings.get("android_out_dir_final")+"//bin//AndroidProject-debug.apk");
			Process executeApk = pbForApk.redirectErrorStream(true).start();
			int retValForApk = executeApk.waitFor();
			if (retValForApk != 0) {
				throw new CommandException("The command execution returned "
						+ retValForApk + " as return value... !\n");
						}
			return runApkOnPhone();
		}
		catch (IOException e) {
			throw new CommandException(e);
		} catch (InterruptedException e) {
			throw new CommandException("It seems the process was killed", e);
		}
	}
	

	String runApkOnPhone()
	{
			List<String> paramsToRunApk = new ArrayList<String>();
			paramsToRunApk.add("adb");
			paramsToRunApk.add("shell");
			paramsToRunApk.add("am");
			paramsToRunApk.add("start");
			paramsToRunApk.add("-n");
			paramsToRunApk.add("com.google.test/.TestActivity");
			return Command.exec(new File(Settings.get("android_sdk_path")), paramsToRunApk);
		}
	}
	
