package gool.generator.csharp;

import gool.Settings;
import gool.executor.SpecificCompiler;
import gool.executor.csharp.CSharpCompiler;
import gool.generator.Platform;
import gool.generator.common.CodePrinter;

import java.io.File;
import java.util.ArrayList;

public final class CSharpPlatform extends Platform {
	private final String outputDir = Settings.get("csharp_out_dir");
	private CSharpPlatform() {
		super("CSHARP");
	}
	
	@Override
	protected CodePrinter initializeCodeWriter() {
		return new CSharpCodePrinter(new File(outputDir));
	}
	
	@Override
	protected SpecificCompiler initializeCompiler() {
		return new CSharpCompiler(new File(outputDir), new ArrayList<File>());
	}

	private static CSharpPlatform instance = new CSharpPlatform();


	public static CSharpPlatform getInstance() {
		return instance;
	}

	public static void newInstance() {
		instance = new CSharpPlatform();
	}
}
