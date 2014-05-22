package codeanalyzer.module.books.views;

import codeanalyzer.core.models.ModelObject;
import codeanalyzer.module.books.list.BookInfo;

public class BookViewModel extends ModelObject {

	public BookInfo data;

	public BookViewModel(BookInfo data) {
		super();
		this.data = data;
	}

	public BookInfo getData() {
		return data;
	}

}
