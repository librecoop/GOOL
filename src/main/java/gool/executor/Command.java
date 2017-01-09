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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
			int i=1;
			Log.d("yo" + i++);
			ProcessBuilder pb = new ProcessBuilder(params);
			Log.d("yo" + i++);
			pb.directory(workingDir);
			Log.d("yo" + i++);
			for (Entry<String, String> e : env.entrySet()) {
				pb.environment().put(e.getKey(), e.getValue());
			}
			Log.d("yo" + i++);
			for(String cmd : pb.command())
				Log.d(cmd);
			Process p = pb.redirectErrorStream(true).start();
			Log.d("yo" + i++);
			p.getOutputStream().close();
			Log.d("yo" + i++);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			Log.d("yo" + i++);
			String line;
			while ((line = in.readLine()) != null) {
				buffer.append(line).append("\n");
			}
			Log.d("yo" + i++);
			int retval = p.waitFor();
			Log.d("yo" + i++);
			if (retval != 0) {
				throw new CommandException("The command execution returned "
						+ retval + " as return value... !\n" + buffer);
			}
			Log.d("yo" + i++);
			return buffer.toString();
		} catch (IOException e) {
			throw new CommandException(e);
		} catch (InterruptedException e) {
			throw new CommandException("It seems the process was killed", e);
		}
	}
}
