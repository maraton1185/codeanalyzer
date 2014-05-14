package codeanalyzer.core.db.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import codeanalyzer.books.book.CurrentBookInfo;
import codeanalyzer.core.db.model.BookInfo;

public interface IDbManager {

	void init() throws InvocationTargetException;

	List<BookInfo> getBooks();

	void addBook(CurrentBookInfo book, BookInfo parent);

}
