package gool.executor.common;

import gool.ast.constructs.ClassMain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public abstract class SpecificCompiler {

	public static Logger logger = Logger.getLogger(SpecificCompiler.class.getName());
	/**
	 * The output directory.
	 */
	private File currentDir;
	private List<File> dependencies = new ArrayList<File>();

	/**
	 * The main class name.
	 */
	private String mainClassName;

	/**
	 * Creates a new compiler with a specific main class and output directory.
	 * 
	 * @param dir
	 * @param deps
	 */
	public SpecificCompiler(File dir, List<File> deps) {
		this.currentDir = dir;
		this.dependencies = deps;
	}

	public static void cleanOutDir(File outDir) {

		
		File[] binFiles = outDir.listFiles();
		if (binFiles != null) {
			for (File file : binFiles) {
				file.delete();
			}
		}
	}

	public void addDependencies(List<File> files) {
		dependencies.addAll(files);
	}

	public abstract File compileToExecutable(List<File> files, File mainFile, List<File> classPath, List<String> args) throws FileNotFoundException;


	public void compileUtils() {
	}

	/**
	 * Writes the generated files into the output directory.
	 * 
	 * @throws FileNotFoundException
	 */
	public void generateFiles(ClassMain mainClass) throws FileNotFoundException {
		getOutputDir().mkdirs();
		cleanOutDir(getOutputDir());
	}

	/**
	 * Returns the list of dependencies that are needed to compile files.
	 * 
	 * @return a list of files.
	 */
	public List<File> getDependencies() {
		return dependencies;
	}
	
	/**
	 * Gets the main class name.
	 * 
	 * @return the class name.
	 */
	public final String getMainClassName() {
		return mainClassName;
	}

	/**
	 * Gets the output directory.
	 * 
	 * @return the output directory.
	 */
    public final File getOutputDir() {
        if (!currentDir.exists())
                currentDir.mkdirs();
         return currentDir;
    }

	public abstract String getSourceCodeExtension();

	public String run(File file) throws FileNotFoundException {
		return run(file, null);
	}

	public abstract String run(File file, List<File> classPath)
			throws FileNotFoundException;

	/**
	 * Sets the main class.
	 * 
	 * @param mainClassName
	 *            the class name.
	 */
	public final void setMainClassName(String mainClassName) {
		this.mainClassName = mainClassName;
	}
	/**
	 * Sets the output directory.
	 * 
	 * @param outputDir
	 *            a File pointing to an existing directory.
	 */
	public final void setOutputDir(File outputDir) {
		this.currentDir = outputDir;
	}
	
	public abstract File compileToObjectFile(List<File> files, File mainFile,
			List<File> classPath, List<String> args) throws FileNotFoundException;
	
	
}
