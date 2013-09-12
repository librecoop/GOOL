/*
 * Copyright 2010 Pablo Arrighi, Alex Concha, Miguel Lezama for version 1.
 * Copyright 2013 Pablo Arrighi, Miguel Lezama, Kevin Mazet for version 2.    
 *
 * This file is part of GOOL.
 *
 * GOOL is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, version 3.
 *
 * GOOL is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License version 3 for more details.
 *
 * You should have received a copy of the GNU General Public License along with GOOL,
 * in the file COPYING.txt.  If not, see <http://www.gnu.org/licenses/>.
 */

package gool.executor.android;

import gool.Settings;
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
			/*
			 * This waits until there is an output on the logcat file before
			 * continuing as it may take some time, limited to a maximum of 10
			 * seconds
			 */
			do {
				Thread.sleep(2000);
				// Reads the logCat file and returns the SysOut equivalent
				returnString = execLogCatCommand("adb logcat -d raw JUnitSysOut:I *:S");
				waitTime++;
			} while (returnString.equals("") || waitTime > 10);
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
		// Some formating is used to remove the signature
		String pattern = "\n?I/JUnitSysOut\\(\\s*\\d*\\): ";
		// String formattedString =
		// returnString.replaceAll("I/JUnitSysOut(*): ", "");

		return returnString.replaceAll(pattern, "");

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
	}
}
