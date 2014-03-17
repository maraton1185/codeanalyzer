package codeanalyzer.core.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;

public interface IBookManager {

	void addBook(String value) throws InvocationTargetException;

	List<BookInfo> getBooks();

	void openBook(IPath path, Shell shell);

	void openBook(BookInfo book, Shell shell);

	boolean saveBook(Shell shell);

	// *********************************************

	List<BookSection> getSections(BookInfo book);

	List<BookSection> getChildren(BookInfo book, BookSection section);

	BookSection getParent(BookInfo book, BookSection section);

	boolean hasChildren(BookInfo book, BookSection section);

	// void showBook(BookInfo book);

}
