package gool.platform.gool;

import gool.platform.Platform;
import gool.platform.common.CodePrinter;
import gool.platform.common.SpecificCompiler;
import gool.util.Settings;

import java.io.File;

public final class GoolPlatform extends Platform {
	private static GoolPlatform instance = new GoolPlatform();
	private String outputDir = Settings.get("gool_out_dir");

	private GoolPlatform() {
		super("GOOL");
	}

	@Override
	protected CodePrinter initializeCodeWriter() {
		return new GoolCodePrinter(new File(outputDir));
	}

	@Override
	protected SpecificCompiler initializeCompiler() {
		throw new IllegalStateException("This method should not be called");
		//return new GoolCompiler(new File(outputDir), new ArrayList<File>());
	}

	public static GoolPlatform getInstance() {
		return instance;
	}

	public static void newInstance() {
		instance = new GoolPlatform();
	}
}
