package ebook.module.conf.model;

import java.util.ArrayList;
import java.util.Collections;

public class SortedArrayList<T> extends ArrayList<T> {

	private static final long serialVersionUID = 2685209775196217575L;

	@SuppressWarnings("unchecked")
	public void insertSorted(T value) {
		super.add(value);
		Comparable<T> cmp = (Comparable<T>) value;
		for (int i = size() - 1; i > 0 && cmp.compareTo(get(i - 1)) < 0; i--)
			Collections.swap(this, i, i - 1);
	}

	// @Override
	// public void add(int index, T element) {
	// throw new UnsupportedOperationException();
	// }
	//
	// @Override
	// public boolean add(T e) {
	// throw new UnsupportedOperationException();
	// }

}
