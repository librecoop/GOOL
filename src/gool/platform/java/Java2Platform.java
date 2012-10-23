package gool.platform.java;

import gool.platform.Platform;
import gool.platform.common.CodePrinter;
import gool.platform.common.SpecificCompiler;
import gool.platform.java.compilation.JavaCompiler;
import gool.platform.java.generation.JavaCodePrinter;
import gool.util.Settings;

import java.io.File;
import java.util.ArrayList;

public final class Java2Platform extends Platform {
	private Java2Platform() {
		super("JAVA2");
	}
	
	@Override
	protected CodePrinter initializeCodeWriter() {
		return new JavaCodePrinter(new File(outputDir));
	}
	
	@Override
	protected SpecificCompiler initializeCompiler() {
		return new JavaCompiler(new File(outputDir), new ArrayList<File>());
	}
	private static Java2Platform instance = new Java2Platform();
	private String outputDir = Settings.get("java2_out_dir");


	public static Java2Platform getInstance() {
		return instance;
	}

}
