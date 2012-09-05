package gool.platform.java;

import gool.platform.Platform;
import gool.platform.common.CodePrinter;
import gool.platform.common.SpecificCompiler;
import gool.platform.java.generation.JavaCodePrinter;
import gool.util.Settings;

import java.io.File;

public final class Java3Platform extends Platform {
	private Java3Platform() {
		super("JAVA3");
	}
	
	@Override
	protected CodePrinter initializeCodeWriter() {
		String outputDir = Settings.getInstance().get("java3_out_dir");
		return new JavaCodePrinter(new File(outputDir));
	}
	
	@Override
	protected SpecificCompiler initializeCompiler() {
		// TODO Auto-generated method stub
		return null;
	}

		private static Java3Platform instance = new Java3Platform();


	public static Platform getInstance() {
		return instance;
	}

}
