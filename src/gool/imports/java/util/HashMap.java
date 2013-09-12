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

public class HashMap<K, T> implements Iterable<HashMap.Entry<K, T>> {

	public static class Entry<K, T> {

		public K getKey() {
			return null;
		}

		public T getValue() {
			return null;
		}

	}

	public HashMap() {
		super();
	}

	public MapIterator<K, T> newEntryIterator() {
		return null;
	}

	public boolean containsKey(Object t) {
		return false;
	}

	public T get(Object type) {
		return null;
	}

	public void put(K key, T value) {
	}

	public boolean isEmpty() {
		return false;
	}

	public void putAll(HashMap<K, T> substitutionMap) {
	}

	public void remove(Object key) {
	}

	public Iterable<Entry<K, T>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Iterator<Entry<K, T>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * public boolean containsValue(Object a){ return false; }
	 */

}
