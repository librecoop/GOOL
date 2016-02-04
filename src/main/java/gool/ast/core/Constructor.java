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

import gool.ast.type.TypeNone;

import java.util.ArrayList;
import java.util.List;

/**
 * This class accounts for method declarations in the intermediate language.
 * Hence it is an OOTDec.
 * 
 * @param T
 *            is the return type, if known at compile time, otherwise put
 *            OOTType. That way java generics grant us some level of type
 *            checking of the generated code at compiler design time. Sometimes
 *            we will not be able to use this though, because we will not know T
 *            at compiler design time.
 */
public class Constructor extends Meth {

	/**
	 * The constructor parent calls
	 */
	private List<InitCall> initCalls = new ArrayList<InitCall>();

	/**
	 * The constructor of a constructor representation.
	 */
	public Constructor() {
		super(TypeNone.INSTANCE, Modifier.PUBLIC, "init");
	}

	/**
	 * Add a statement in the constructor representation.
	 * @param initCall
	 * 		: The statement of the constructor representation.
	 */
	public void addInitCall(InitCall initCall) {
		initCalls.add(initCall);
	}

	/**
	 * Determines whether it is a constructor representation.
	 * @return
	 * 		true if it is a constructor representation.
	 */
	public boolean isConstructor() {
		return true;
	}

	/**
	 * Gets the statements list of a constructor representation.
	 * @return
	 * 		The statements list of a constructor representation.
	 */
	public List<InitCall> getInitCalls() {
		return initCalls;
	}

}
