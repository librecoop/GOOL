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

/**
 * A dependency is the abstract GOOL representation of the existence of an
 * external class which is used in the code of some abstract GOOL class Gets
 * implemented by CustomDependency TODO: Maybe the two could be merged
 */
public abstract class Dependency extends Node {

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Dependency) || !(obj instanceof String)) {
			return false;
		} else if (obj instanceof String) {
			return toString().equals(obj.toString());
		}
		return toString().equals(((Dependency) obj).toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
