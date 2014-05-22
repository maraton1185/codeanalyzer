package codeanalyzer.module.books.interfaces;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.module.books.list.BookInfo;
import codeanalyzer.module.books.list.BookInfoSelection;

public interface IBookManager {

	void addBook(String value, BookInfo parent)
			throws InvocationTargetException;

	// List<CurrentBookInfo> getBooks();

	void openBook(IPath path, Shell shell);

	// void openBook(WindowBookInfo book, Shell shell);

	// boolean saveBook(BookInfo book, Shell shell);

	void addGroup(BookInfo data, BookInfo book, boolean sub)
			throws InvocationTargetException;

	void delete(BookInfoSelection selection);

	void addBookToList(IPath p, BookInfo book) throws InvocationTargetException;

	boolean save(BookInfo data, Shell shell);

}
