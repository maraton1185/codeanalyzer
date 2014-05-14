package codeanalyzer.books.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.books.book.CurrentBookInfo;
import codeanalyzer.core.model.BookInfo;

public interface IBookManager {

	void addBook(String value, BookInfo parent)
			throws InvocationTargetException;

	List<CurrentBookInfo> getBooks();

	void openBook(IPath path, Shell shell);

	void openBook(CurrentBookInfo book, Shell shell);

	boolean saveBook(CurrentBookInfo book, Shell shell);

}
