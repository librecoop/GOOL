package gool.generator.android;

import gool.Settings;
import gool.executor.android.AndroidCompiler;
import gool.executor.common.SpecificCompiler;
import gool.generator.common.CodePrinter;
import gool.generator.common.Platform;
import java.io.File;
import java.util.ArrayList;

public class AndroidPlatform extends Platform {

	private final String outputDir = Settings.get("android_out_dir");

	private AndroidPlatform() {
		super("ANDROID");
	}
	
	@Override
	protected CodePrinter initializeCodeWriter() {
		return new AndroidCodePrinter(new File(outputDir));
	}
	
	@Override
	protected SpecificCompiler initializeCompiler() {
		return new AndroidCompiler(new File(outputDir), new ArrayList<File>());
	}
	
	/**
	 * What follows is to make sure that we always use the same instance of this object.
	 */
	
	private static AndroidPlatform instance = new AndroidPlatform();

	public static AndroidPlatform getInstance() {
		return instance;
	}
	public static void newInstance() {
		instance = new AndroidPlatform();
	}
}

