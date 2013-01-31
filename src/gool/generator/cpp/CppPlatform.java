/**
 * Once can have several C++ platforms, i.e. with different compilers etc.
 */


package gool.generator.cpp;

import gool.Settings;
import gool.executor.common.SpecificCompiler;
import gool.executor.cpp.CppCompiler;
import gool.generator.common.CodePrinter;
import gool.generator.common.Platform;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class CppPlatform extends Platform {
	private final String outputDir = Settings.get("cpp_out_dir");

	public CppPlatform(Collection<File> myFile) {
		super("CPP", myFile);
	}
	
	@Override
	protected CodePrinter initializeCodeWriter() {
		return new CppCodePrinter(new File(outputDir), myFileToCopy);
	}
	
	@Override
	protected SpecificCompiler initializeCompiler() {
		return new CppCompiler(new File(outputDir), new ArrayList<File>());
	}
	private static CppPlatform instance = new CppPlatform(myFileToCopy);


	public static CppPlatform getInstance(Collection<File> myF) {
		myFileToCopy = myF;
		return instance;
	}
	
	public static CppPlatform getInstance() {
		if(myFileToCopy == null) {
			myFileToCopy = new ArrayList<File>();
		}
		return instance;
	}

	public static void newInstance() {
		instance = new CppPlatform(myFileToCopy);
	}

}
