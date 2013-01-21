package gool.executor.android;

import gool.Settings;
import gool.ast.system.SystemOutPrintCall;
import gool.executor.Command;
import gool.executor.CommandException;
import gool.executor.common.SpecificCompiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
		params.add("ant");
		params.add("debug");

		Command.exec(new File(Settings.get("android_out_dir_final")), params);
		return (new File(Settings.get("android_out_dir_final")));
	}

	@Override
	public File compileToObjectFile(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
			throws FileNotFoundException {
		return compileToExecutable(files, mainFile, classPath, args);
	}

	@Override
	public String run(File file, List<File> classPath)
			throws FileNotFoundException {
		try {

			List<String> paramsToRunEmulator = new ArrayList<String>();
			ProcessBuilder pbForEmulator = new ProcessBuilder(
					paramsToRunEmulator);
			paramsToRunEmulator.add("android");
			paramsToRunEmulator.add("avd");
			pbForEmulator.directory(new File(Settings.get("android_sdk_path")));
			Process avdManagerStart = pbForEmulator.redirectErrorStream(true)
					.start();
			int retval = avdManagerStart.waitFor();
			if (retval != 0) {
				throw new CommandException("The command execution returned "
						+ retval + " as return value... !\n");
			}
			installApkOnPhone();
			execLogCatCommand("adb logcat -c");
			runApkOnPhone();
			String returnString;
			int waitTime = 0;
			/*This waits until there is an output on the logcat file before continuing
			 * as it may take some time, limited to a maximum of 10 seconds
			 */			
			do {
				Thread.sleep(1000);
				// Reads the logCat file and returns the SysOut equivalent
			returnString = execLogCatCommand("adb logcat -d raw JUnitSysOut:I *:S"); 
			waitTime++;
			}
			while(returnString.equals("")||waitTime>20);
			System.out.println("Waited "+waitTime/2+" seconds for logcat output:" + returnString);
			return returnString;
		} catch (IOException e) {
			throw new CommandException(e);
		} catch (InterruptedException e) {
			throw new CommandException("It seems the process was killed", e);
		}
		

	}

	/**
	 * Method used to execute logCat command
	 * 
	 * @param executeCommand
	 * @return
	 */
	private String execLogCatCommand(String executeCommand) {
		String returnString = null;
		try {
			// String executeCommand = "adb logcat -d raw JUnitSysOut:I *:S";
			Process waitForProcess = Runtime.getRuntime().exec(executeCommand);

			waitForProcess.waitFor();
			BufferedReader buf = new BufferedReader(new InputStreamReader(
					waitForProcess.getInputStream()));
			String line = "";
			String output = "";
			while ((line = buf.readLine()) != null) {
				output += line + "\n";
			}

			returnString = output;

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		} catch (InterruptedException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		return returnString.substring(returnString.indexOf(":") + 1);

	}

	@Override
	public String getSourceCodeExtension() {
		// TODO Auto-generated method stub
		return null;
	}

	void installApkOnPhone() {
		try {

			List<String> paramsToInstallApk = new ArrayList<String>();
			ProcessBuilder pbForApk = new ProcessBuilder(paramsToInstallApk);
			paramsToInstallApk.add("adb");
			paramsToInstallApk.add("install");
			paramsToInstallApk.add("-r");
			paramsToInstallApk.add(Settings.get("android_out_dir_final")
					+ "//bin//AndroidProject-debug.apk");
			Process executeApk = pbForApk.redirectErrorStream(true).start();
			int retValForApk = executeApk.waitFor();
			if (retValForApk != 0) {
				throw new CommandException("The command execution returned "
						+ retValForApk + " as return value... !\n");
			}

		} catch (IOException e) {
			throw new CommandException(e);
		} catch (InterruptedException e) {
			throw new CommandException("It seems the process was killed", e);
		}
	}

	void runApkOnPhone() {
		List<String> paramsToRunApk = new ArrayList<String>();
		paramsToRunApk.add("adb");
		paramsToRunApk.add("shell");
		paramsToRunApk.add("am");
		paramsToRunApk.add("start");
		paramsToRunApk.add("-n");
		String mainActivity = Settings.getAndroidRunCommand();
		paramsToRunApk.add(mainActivity);
		String runApkResult = Command.exec(
				new File(Settings.get("android_sdk_path")), paramsToRunApk);
		System.out.println(runApkResult);
	}
}
