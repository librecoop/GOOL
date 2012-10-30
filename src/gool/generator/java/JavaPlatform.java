package gool.generator.java;

import gool.Settings;
import gool.executor.SpecificCompiler;
import gool.executor.java.JavaCompiler;
import gool.generator.Platform;
import gool.generator.common.CodePrinter;

import java.io.File;
import java.util.ArrayList;

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
	private static JavaPlatform instance = new JavaPlatform();


	public static JavaPlatform getInstance() {
		return instance;
	}

	public static void newInstance() {
		instance = new JavaPlatform();
	}
}
