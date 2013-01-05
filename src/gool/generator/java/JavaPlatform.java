package gool.generator.java;

import gool.Settings;
import gool.executor.common.SpecificCompiler;
import gool.executor.java.JavaCompiler;
import gool.generator.common.CodePrinter;
import gool.generator.common.Platform;

import java.io.File;
import java.util.ArrayList;

/**
 * This is the default Java Platform.
 * It uses the output folders specified in Settings.
 * It uses JavaCodePrinter for concrete Java generation.
 * It uses JavaCompiler for compiling and executing the generated concrete Java.
 * Since it is fully specified, and since there should only ever be one instance of it, this could almost have been a static class.
 * But since platforms in general are objects, this is not the case.
 * Still, we make sure that there is always only one instance of this object.
 */
public final class JavaPlatform extends Platform {

	private final String outputDir = Settings.get("java_out_dir");

	private JavaPlatform() {
		super("JAVA");
	}
	
	@Override
	protected CodePrinter initializeCodeWriter() {
		return new JavaCodePrinter(new File(outputDir));
	}
	
	@Override
	protected SpecificCompiler initializeCompiler() {
		return new JavaCompiler(new File(outputDir), new ArrayList<File>());
	}
	
	/**
	 * What follows is to make sure that we always use the same instance of this object.
	 */
	
	private static JavaPlatform instance = new JavaPlatform();

	public static JavaPlatform getInstance() {
		return instance;
	}
	public static void newInstance() {
		instance = new JavaPlatform();
	}
}
