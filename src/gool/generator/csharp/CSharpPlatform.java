package gool.generator.csharp;

import gool.Settings;
import gool.executor.common.SpecificCompiler;
import gool.executor.csharp.CSharpCompiler;
import gool.generator.common.CodePrinter;
import gool.generator.common.Platform;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public final class CSharpPlatform extends Platform {
	private final String outputDir = Settings.get("csharp_out_dir");
	private CSharpPlatform(Collection<File> myFile) {
		super("CSHARP", myFile);
	}
	
	@Override
	protected CodePrinter initializeCodeWriter() {
		//TODO a voir pour passer la liste des fichiers
		return new CSharpCodePrinter(new File(outputDir), null);
	}
	
	@Override
	protected SpecificCompiler initializeCompiler() {
		return new CSharpCompiler(new File(outputDir), new ArrayList<File>());
	}

	private static CSharpPlatform instance = new CSharpPlatform(myFileToCopy);


	public static CSharpPlatform getInstance(Collection<File> myF) {
		myFileToCopy = myF;
		return instance;
	}
	
	public static CSharpPlatform getInstance() {
		if(myFileToCopy == null) {
			myFileToCopy = new ArrayList<File>();
		}
		return instance;
	}

	public static void newInstance() {
		instance = new CSharpPlatform(myFileToCopy);
	}
}
