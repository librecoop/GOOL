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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Operating System command execution helper.
 */
public final class Command {
	private static Logger logger = Logger.getLogger(Command.class.getName());
	private Command() {
	}

	/**
	 * Executes a command in the specified working directory.
	 * @param workingDir the working directory.
	 * @param params the command to execute and its parameters.
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
	 * @param workingDir the working directory.
	 * @param params the command to execute and its parameters.
	 * @return the console output.
	 */
	public static String exec(File workingDir, List<String> params, Map<String, String> env) {
		try {
			logger.info(StringUtils.join(params.toArray(), " "));
			StringBuffer buffer = new StringBuffer();

			ProcessBuilder pb = new ProcessBuilder(params);
			pb.directory(workingDir);

			for (Entry<String, String> e : env.entrySet()) {
				pb.environment().put(e.getKey(), e.getValue());
			}
			
			Process p = pb.redirectErrorStream(true).start();

			p.getOutputStream().close();
			BufferedReader in = new BufferedReader(new InputStreamReader(p
					.getInputStream()));

			String line;
			while ((line = in.readLine()) != null) {
				buffer.append(line).append("\n");
			}

			int retval = p.waitFor();

			if (retval != 0) {
				throw new CommandException("The command execution returned "
						+ retval + " as return value... !\n" + buffer);
			}

			return buffer.toString();
		} catch (IOException e) {
			throw new CommandException(e);
		} catch (InterruptedException e) {
			throw new CommandException("It seems the process was killed", e);
		}
	}
}
