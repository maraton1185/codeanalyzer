package codeanalyzer.module.booksList;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.module.books.list.ListBookInfo;
import codeanalyzer.module.books.list.ListBookInfoSelection;

public interface IBookListManager {

	void addBook(String value, ListBookInfo parent)
			throws InvocationTargetException;

	// List<CurrentBookInfo> getBooks();

	void openBook(IPath path, Shell shell);

	// void openBook(WindowBookInfo book, Shell shell);

	// boolean saveBook(BookInfo book, Shell shell);

	void addGroup(ListBookInfo data, ListBookInfo book, boolean sub)
			throws InvocationTargetException;

	void delete(ListBookInfoSelection selection);

	void addBookToList(IPath p, ListBookInfo book)
			throws InvocationTargetException;

	// boolean save(ListBookInfo data, Shell shell);

	// boolean save(ListBookInfo data, BookInfo info, Shell shell);

	boolean save(ListBookInfo data, Shell shell);

}
