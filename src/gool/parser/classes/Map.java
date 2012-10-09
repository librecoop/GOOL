package gool.parser.classes;

import java.util.Iterator;

public class Map<K, T> implements Iterable<Map.Entry<K, T>> {

	public static class Entry<K, T> {

		public K getKey() {
			return null;
		}

		public T getValue() {
			return null;
		}

	}	

	public Map() {
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

	public void putAll(Map<K, T> substitutionMap) {
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

}
