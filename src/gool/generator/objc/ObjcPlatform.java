package gool.generator.objc;

import gool.Settings;
import gool.executor.common.SpecificCompiler;
import gool.executor.objc.ObjcCompiler;
import gool.generator.common.CodePrinter;
import gool.generator.common.Platform;

import java.io.File;
import java.util.ArrayList;

public class ObjcPlatform extends Platform {
	private final String outputDir = Settings.get("objc_out_dir");

	protected ObjcPlatform() {
		super("OBJC");
	}

	@Override
	protected CodePrinter initializeCodeWriter() {
		return new ObjcCodePrinter(new File(outputDir));
	}

	@Override
	protected SpecificCompiler initializeCompiler() {
		return new ObjcCompiler(new File(outputDir), new ArrayList<File>());
	}

	private static ObjcPlatform instance = new ObjcPlatform();

	public static ObjcPlatform getInstance() {
		return instance;
	}

	public static void newInstance() {
		instance = new ObjcPlatform();
	}

}