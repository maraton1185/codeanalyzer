package codeanalyzer.books.book;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BookInfoSelection {

	public Iterator<BookInfo> iterator() {
		return list.iterator();
	}

	public BookInfoSelection() {
		super();
		list = new ArrayList<BookInfo>();
	}

	private List<BookInfo> list;

	public void add(BookInfo item) {
		list.add(item);

	}

	public int getParent() {
		if (list.isEmpty())
			return 0;

		return list.get(0).parent;
	}
}
