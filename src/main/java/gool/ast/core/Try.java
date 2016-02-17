/*
 * Copyright 2010 Pablo Arrighi, Alex Concha, Miguel Lezama for version 1.
 * Copyright 2013 Pablo Arrighi, Miguel Lezama, Kevin Mazet for version 2.    
 *
 * This file is part of GOOL.
 *
 * GOOL is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, version 3.
 *
 * GOOL is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License version 3 for more details.
 *
 * You should have received a copy of the GNU General Public License along with GOOL,
 * in the file COPYING.txt.  If not, see <http://www.gnu.org/licenses/>.
 */

package gool.ast.core;

import gool.generator.GoolGeneratorController;
import gool.generator.common.CodeGenerator;

import java.util.List;

/**
 * This class captures the use of a block "try/catch/finally".
 */
public class Try extends Statement {
	
	/**
	 * The list of catches used by the "try" block.
	 */
	private List<? extends Catch> catches;

	/**
	 * The block of statements used by the "try" block.
	 */
	private Block block;

	/**
	 * The "finally" block used by the "try" block.
	 */
	private Block finilyBlock;

	/**
	 * The constructor of a "try/catch/finally" representation.
	 * @param catches
	 * 		: The list of catches used by the "try" block.
	 * @param block
	 * 		: The block of statements used by the "try" block.
	 * @param finilyBlock
	 * 		: The "finally" block used by the "try" block.
	 */
	public Try(List<? extends Catch> catches, Block block, Block finilyBlock) {
		this.catches = catches;
		this.block = block;
		this.finilyBlock = finilyBlock;
	}

	/**
	 * Gets the list of catches used by the "try" block.
	 * @return
	 * 		The list of catches used by the "try" block.
	 */
	public List<? extends Catch> getCatches() {
		return catches;
	}

	/**
	 * Gets the block of statements used by the "try" block.
	 * @return
	 * 		The block of statements used by the "try" block.
	 */
	public Block getBlock() {
		return block;
	}

	/**
	 * Gets the "finally" block used by the "try" block.
	 * @return
	 * 		The "finally" block used by the "try" block.
	 */
	public Block getFinilyBlock() {
		return finilyBlock;
	}

	@Override
	public String callGetCode() {
		CodeGenerator cg;
		try{
			cg = GoolGeneratorController.generator();
		}catch (IllegalStateException e){
			return this.getClass().getSimpleName();
		}
		return cg.getCode(this);
	}
}
