package gool.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * A filename filter based on regular expressions. It can be used to list files
 * with filenames matching a certain pattern.
 */
public class RegexFilter implements FilenameFilter {
	/**
	 * The regular expression pattern.
	 */
	private Pattern pattern;

	/**
	 * Creates a filter with the specified regular expression.
	 * 
	 * @param regex
	 *            a regular expression.
	 */
	public RegexFilter(String regex) {
		pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	}

	@Override
	public boolean accept(File dir, String name) {
		return pattern.matcher(name).find();
	}
}