package gool.java.util;


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
