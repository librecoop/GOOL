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

package gool.imports.java.util;

import java.util.Iterator;

public class ArrayList<T> implements Iterable<T> {
	public void add(T element) {
	};

	// The target languages have different return types for the remove method.
	// That is why this method returns nothing.
	public void removeAt(int position) {
	}

	public void remove(T element) {
	}

	public boolean isEmpty() {
		return false;
	}

	public int size() {
		return 0;
	}

	public void add(int i, T mguObject) {
	}

	public T get(int i) {
		return null;
	}

	public ListIterator<T> getIterator() {
		return null;
	}

	public boolean contains(T key) {
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	};
}
