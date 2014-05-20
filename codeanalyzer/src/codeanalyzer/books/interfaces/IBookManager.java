package codeanalyzer.books.interfaces;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.books.book.BookInfo;
import codeanalyzer.books.book.BookInfoSelection;
import codeanalyzer.books.book.CurrentBookInfo;

public interface IBookManager {

	void addBook(String value, BookInfo parent)
			throws InvocationTargetException;

	// List<CurrentBookInfo> getBooks();

	void openBook(IPath path, Shell shell);

	void openBook(CurrentBookInfo book, Shell shell);

	boolean saveBook(CurrentBookInfo book, Shell shell);

	void addGroup(BookInfo data, BookInfo book, boolean sub)
			throws InvocationTargetException;

	void delete(BookInfoSelection selection);

	void addBookToList(IPath p, BookInfo book) throws InvocationTargetException;

}
