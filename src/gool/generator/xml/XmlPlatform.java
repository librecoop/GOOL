package gool.generator.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import gool.Settings;
import gool.executor.common.SpecificCompiler;
import gool.generator.common.CodePrinter;
import gool.generator.common.Platform;
import gool.generator.python.PythonPlatform;

public class XmlPlatform extends Platform {
	
	private final String outputDir = Settings.get("xml_out_dir");
	
	protected XmlPlatform(Collection<File> myFile) {
		super("XML", myFile);
	}

	@Override
	protected CodePrinter initializeCodeWriter() {
		return new XmlCodePrinter(new File(outputDir), myFileToCopy);
	}

	@Override
	protected SpecificCompiler initializeCompiler() {
		return null;
	}
	
	private static XmlPlatform instance = new XmlPlatform(myFileToCopy);


	
	public static XmlPlatform getInstance(Collection<File> myF) {
		myFileToCopy = myF;
		return instance;
	}
	
	public static XmlPlatform getInstance() {
		if(myFileToCopy == null) {
			myFileToCopy = new ArrayList<File>();
		}
		return instance;
	}
	public static void newInstance() {
		instance = new XmlPlatform(myFileToCopy);
	}

}
