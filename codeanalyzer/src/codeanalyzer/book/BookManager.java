package codeanalyzer.book;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.book.services.BookSectionsService;
import codeanalyzer.book.services.BookService;
import codeanalyzer.book.services.BookStructure;
import codeanalyzer.core.AppManager;
import codeanalyzer.core.interfaces.IBookManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;

public class BookManager implements IBookManager {

	BookStructure bookStructure = new BookStructure();
	BookService bs = new BookService();
	BookSectionsService bookSections;

	BookService books;

	@Override
	public BookSectionsService sections() {

		bookSections = bookSections == null ? new BookSectionsService()
				: bookSections;

		return bookSections;
	}

	@Override
	public void addBook(String value) throws InvocationTargetException {

		BookInfo book = new BookInfo();

		book.setName(value);

		try {
			Connection con = null;
			try {
				con = book.makeConnection(false);
				bookStructure.createStructure(con, book);
				bs.getData(con, book);
				book.setOpened(true);
				AppManager.ctx.set(BookInfo.class, book);
			} finally {
				con.close();
			}
		} catch (Exception e) {
			throw new InvocationTargetException(e);
		}

		AppManager.br.post(Const.EVENT_UPDATE_BOOK_LIST, null);
		AppManager.br.post(Const.EVENT_UPDATE_BOOK_INFO, null);

	}

	@Override
	public List<BookInfo> getBooks() {

		List<BookInfo> result = new ArrayList<BookInfo>();

		File folder = new Path(
				PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY))
				.toFile();
		File[] files = folder.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {

				String name = file.getName();
				return name.contains(".h2.db");
			}
		});

		if (files == null)
			return result;

		for (File f : files) {
			BookInfo book = new BookInfo();
			book.setPath(new Path(f.getPath()));
			result.add(book);
		}

		return result;
	}

	@Override
	public void openBook(IPath path, Shell shell) {

		BookInfo book = new BookInfo();

		book.setPath(path);

		openBook(book, shell);

	}

	@Override
	public void openBook(BookInfo book, Shell shell) {

		try {

			Connection con = null;
			try {
				con = book.makeConnection(true);
				bookStructure.checkSructure(con, book);
				bs.getData(con, book);
				book.setOpened(true);

			} finally {
				con.close();
			}

			AppManager.ctx.set(BookInfo.class, book);

		} catch (Exception e) {

			AppManager.ctx.set(BookInfo.class, null);
			if (shell != null)
				MessageDialog.openError(shell, Strings.get("appTitle"),
						"Ошибка открытия книги.");
		}
		AppManager.br.post(Const.EVENT_UPDATE_BOOK_INFO, null);
		// AppManager.br.post(Const.EVENT_SHOW_BOOK, null);
	}

	@Override
	public boolean saveBook(BookInfo book, Shell shell) {

		try {
			Connection con = null;
			try {
				con = book.makeConnection(true);
				bookStructure.checkSructure(con, book);
				bs.setData(con, book);

			} finally {
				con.close();
			}

		} catch (Exception e) {
			MessageDialog.openError(shell, Strings.get("appTitle"),
					"Ошибка сохранения книги.");
			return false;
		}

		return true;
	}

}
