package gool.ast;

import gool.platform.Platform;
import gool.platform.common.CodePrinter;

public class ClassMain extends ClassDef {

	private Block mainBlock;
	
	public ClassMain(String name, Platform platform) {
		super(Modifier.PUBLIC, name, platform);
	}

	public Block getMainBlock() {
		return mainBlock;
	}
	
	public String getCode(CodePrinter codePrinter) {
		return codePrinter.processTemplate("mainClass.vm", this);
	}

	public void setMainBlock(Block mainBlock) {
		this.mainBlock = mainBlock;
	}
	
}
