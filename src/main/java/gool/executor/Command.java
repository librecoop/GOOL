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

package gool.executor;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import logger.Log;

/**
 * Operating System command execution helper.
 */
public final class Command {
	private Command() {
	}

	/**
	 * Executes a command in the specified working directory.
	 * 
	 * @param workingDir
	 *            the working directory.
	 * @param params
	 *            the command to execute and its parameters.
	 * @return the console output.
	 */
	public static String exec(File workingDir, String... params) {
		return exec(workingDir, Arrays.asList(params));
	}

	public static String exec(File workingDir, List<String> params) {
		return exec(workingDir, params, new HashMap<String, String>());
	}

	/**
	 * Executes a command in the specified working directory.
	 * 
	 * @param workingDir
	 *            the working directory.
	 * @param params
	 *            the command to execute and its parameters.
	 * @return the console output.
	 */
	public static String exec(File workingDir, List<String> params,
			Map<String, String> env) {
		try {
			StringBuffer buffer = new StringBuffer();
			Log.d("<gool.executor - exec> Entering");
			ProcessBuilder pb = new ProcessBuilder(params);
			Log.d("<gool.executor - exec> ProcessBuilder initialized");
			pb.directory(workingDir);
			Log.d("<gool.executor - exec> Working directory = " + workingDir.getAbsolutePath());
			for (Entry<String, String> e : env.entrySet()) {
				pb.environment().put(e.getKey(), e.getValue());
			}

			for(String cmd : pb.command())
				Log.d("<gool.executor - exec> " + cmd);
			Process p = pb.redirectErrorStream(true).start();

			p.getOutputStream().close();

			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line;
			while ((line = in.readLine()) != null) {
				buffer.append(line).append("\n");
			}

			int retval = p.waitFor();

			if (retval != 0) {
				throw new CommandException("The command execution returned "
						+ retval + " as return value... !\n" + buffer);
			}
			Log.d("<gool.executor - exec> Closure");
			return buffer.toString();
		} catch (IOException e) {
			throw new CommandException(e);
		} catch (InterruptedException e) {
			throw new CommandException("It seems the process was killed", e);
		}
	}

	/**
	 * Execute the one command line build for docker.
	 * @param command line
	 * @return a list with two strings. The first one corresponds to the standard output and the second one to the standard error.
	 */
	public static List<String> execDocker(String command) {
		Runtime runtime = Runtime.getRuntime();
		final Process process;
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
	    PrintStream stdoutps = new PrintStream(stdout);
	    ByteArrayOutputStream stderr = new ByteArrayOutputStream();
	    PrintStream stderrps = new PrintStream(stderr);
		try {
			process = runtime.exec(new String [] {"/bin/bash","-c", command});
			
			// Std output redirection
			new Thread() {
				public void run() {
					try {
						BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
						String line = "";
						try {
							while((line = reader.readLine()) != null) {
								stdoutps.println(line);
								//System.out.println(line);
							}
						} finally {
							reader.close();
						}
					} catch(IOException ioe) {
						ioe.printStackTrace();
					}
				}
			}.start();

			// Std error redirection
			new Thread() {
				public void run() {
					try {
						BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
						String line = "";
						try {
							while((line = reader.readLine()) != null) {
								stderrps.println(line);
								//System.err.println(line);
							}

						} finally {
							reader.close();
						}
					} catch(IOException ioe) {
						ioe.printStackTrace();
					}
				}
			}.start();
		} catch (Exception e) {
			throw new CommandException(e);
		}
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			throw new CommandException("The docker command execution failed\n", e);
		}
		return Arrays.asList(new String(stdout.toByteArray(), StandardCharsets.UTF_8),
				new String(stderr.toByteArray(), StandardCharsets.UTF_8));
	}
}
