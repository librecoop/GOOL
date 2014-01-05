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
