package gool.ast.constructs;

import gool.generator.common.CodePrinter;
import gool.generator.common.Platform;

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
