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

package gool.ast.system;

import gool.ast.core.GoolCall;
import gool.ast.type.TypeVoid;
import gool.generator.GoolGeneratorController;
import gool.generator.common.CodeGenerator;

/**
 * This class captures the invocation of a system method for out print.
 */
public class SystemOutPrintCall extends GoolCall {
	
	/**
	 * Flag that indicates if the print has to end with an end of line or not
	 */
	boolean endofline = false;

	public boolean isEndofline() {
		return endofline;
	}

	public void setEndofline(boolean endofline) {
		this.endofline = endofline;
	}

	/**
	 * The constructor of a "system out print call" representation.
	 */
	public SystemOutPrintCall() {
		super(TypeVoid.INSTANCE);
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
