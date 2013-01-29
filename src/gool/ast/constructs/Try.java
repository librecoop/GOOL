package gool.ast.constructs;

import gool.generator.GoolGeneratorController;

import java.util.List;

public class Try extends Statement {
	/**
	 * 
	 */
	private List<? extends Catch> catches;
	
	/**
	 * 
	 */
	private Block block;

	/**
	 * 
	 */
	private Block finilyBlock;
	
	public Try(List<? extends Catch> catches, Block block, Block finilyBlock) {
		this.catches = catches;
		this.block = block;
		this.finilyBlock = finilyBlock;
	}
	
	public List<? extends Catch> getCatches() {
		return catches;
	}

	public Block getBlock() {
		return block;
	}

	public Block getFinilyBlock() {
		return finilyBlock;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
