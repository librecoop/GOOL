package gool.generator.xml;

import java.io.File;
import gool.Settings;
import gool.executor.common.SpecificCompiler;
import gool.generator.common.CodePrinter;
import gool.generator.common.Platform;

public class XmlPlatform extends Platform {
	
	private final String outputDir = Settings.get("xml_out_dir");
	
	protected XmlPlatform() {
		super("XML");
	}

	@Override
	protected CodePrinter initializeCodeWriter() {
		return new XmlCodePrinter(new File(outputDir));
	}

	@Override
	protected SpecificCompiler initializeCompiler() {
		return null;
	}
	
	private static XmlPlatform instance = new XmlPlatform();

	public static XmlPlatform getInstance() {
		return instance;
	}
	public static void newInstance() {
		instance = new XmlPlatform();
	}

}
