package gool.platform.cpp;

import gool.platform.Platform;
import gool.platform.common.CodePrinter;
import gool.platform.common.SpecificCompiler;
import gool.platform.cpp.compilation.CppCompiler;
import gool.platform.cpp.generation.CppCodePrinter;
import gool.platform.csharp.compilation.CSharpCompiler;
import gool.platform.java.generation.JavaCodePrinter;
import gool.util.Settings;

import java.io.File;
import java.util.ArrayList;

public class CppPlatform extends Platform {
	private final String outputDir = Settings.getInstance().get("cpp_out_dir");

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
