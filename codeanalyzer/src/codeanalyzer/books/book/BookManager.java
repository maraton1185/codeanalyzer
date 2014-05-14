package codeanalyzer.books.book;

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

import codeanalyzer.books.BookStructure;
import codeanalyzer.books.interfaces.IBookManager;
import codeanalyzer.core.AppManager;
import codeanalyzer.core.pico;
import codeanalyzer.core.model.BookInfo;
import codeanalyzer.db.interfaces.IDbManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;

public class BookManager implements IBookManager {

	IDbManager dbManager = pico.get(IDbManager.class);
	BookStructure bookStructure = new BookStructure();
	BookService bs = new BookService();
	BookService books;

	@Override
	public void addBook(String value, BookInfo parent)
			throws InvocationTargetException {

		CurrentBookInfo book = new CurrentBookInfo();

		book.setName(value);

		try {
			Connection con = null;
			try {
				con = book.makeConnection(false);
				bookStructure.createStructure(con, book);
				bs.getData(con, book);
				book.setOpened(true);
				dbManager.addBook(book, parent);
				AppManager.ctx.set(CurrentBookInfo.class, book);
				AppManager.br.post(Const.EVENT_UPDATE_BOOK_INFO, null);
			} finally {
				con.close();
			}
		} catch (Exception e) {
			throw new InvocationTargetException(e);
		}

	}

	@Override
	public List<CurrentBookInfo> getBooks() {

		List<CurrentBookInfo> result = new ArrayList<CurrentBookInfo>();

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
			CurrentBookInfo book = new CurrentBookInfo();
			book.setPath(new Path(f.getPath()));
			result.add(book);
		}

		return result;
	}

	@Override
	public void openBook(IPath path, Shell shell) {

		CurrentBookInfo book = new CurrentBookInfo();

		book.setPath(path);

		openBook(book, shell);

	}

	@Override
	public void openBook(CurrentBookInfo book, Shell shell) {

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

			AppManager.ctx.set(CurrentBookInfo.class, book);

		} catch (Exception e) {

			AppManager.ctx.set(CurrentBookInfo.class, null);
			if (shell != null)
				MessageDialog.openError(shell, Strings.get("appTitle"),
						"Ошибка открытия книги.");
		}
		AppManager.br.post(Const.EVENT_UPDATE_BOOK_INFO, null);
		// AppManager.br.post(Const.EVENT_SHOW_BOOK, null);
	}

	@Override
	public boolean saveBook(CurrentBookInfo book, Shell shell) {

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
