package gool.generator.cpp;

import gool.Settings;
import gool.executor.SpecificCompiler;
import gool.executor.cpp.CppCompiler;
import gool.executor.csharp.CSharpCompiler;
import gool.generator.Platform;
import gool.generator.common.CodePrinter;
import gool.generator.java.JavaCodePrinter;

import java.io.File;
import java.util.ArrayList;

public class CppPlatform extends Platform {
	private final String outputDir = Settings.get("cpp_out_dir");

	public CppPlatform() {
		super("CPP");
	}
	
	@Override
	protected CodePrinter initializeCodeWriter() {
		return new CppCodePrinter(new File(outputDir));
	}
	
	@Override
	protected SpecificCompiler initializeCompiler() {
		return new CppCompiler(new File(outputDir), new ArrayList<File>());
	}
	private static CppPlatform instance = new CppPlatform();


	public static Platform getInstance() {
		return instance;
	}

	public static void newInstance() {
		instance = new CppPlatform();
	}

}
