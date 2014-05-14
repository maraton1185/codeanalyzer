package codeanalyzer.db.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import codeanalyzer.books.book.CurrentBookInfo;
import codeanalyzer.core.model.BookInfo;

public interface IDbManager {

	void init() throws InvocationTargetException;

	void addBook(CurrentBookInfo book, BookInfo parent);

	void addBooksGroup(String title, BookInfo current, boolean sub);

	void delete(BookInfo book);

	void saveTitle(BookInfo object);

	List<BookInfo> getBooks(int parent);

	boolean hasChildren(int parent);

}
