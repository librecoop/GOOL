/*
 * Copyright 2010 Pablo Arrighi, Alex Concha, Miguel Lezama for version 1 of this file.
 * Copyright 2013 Pablo Arrighi, Miguel Lezama, Kevin Mazet for version 2 of this file.    
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
