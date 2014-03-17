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

	BookInfo info = new BookInfo();

	@Override
	public void addBook(String value) throws InvocationTargetException {

		info.setName(value);

		try {
			Connection con = null;
			try {
				con = info.getConnection(false);
				bookStructure.createStructure(con, info);
				bs.getData(con, info);
				info.setOpened(true);
				AppManager.ctx.set(BookInfo.class, info);
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

		info.setPath(path);

		openBook(info, shell);

	}

	@Override
	public void openBook(BookInfo book, Shell shell) {
		info = book;
		try {

			Connection con = null;
			try {
				con = info.getConnection(true);
				bookStructure.checkSructure(con, info);
				bs.getData(con, info);
				info.setOpened(true);

			} finally {
				con.close();
			}

			AppManager.ctx.set(BookInfo.class, info);

		} catch (Exception e) {

			AppManager.ctx.set(BookInfo.class, null);
			MessageDialog.openError(shell, Strings.get("appTitle"),
					"Ошибка открытия книги.");
		}
		AppManager.br.post(Const.EVENT_UPDATE_BOOK_INFO, null);
		// AppManager.br.post(Const.EVENT_SHOW_BOOK, null);
	}

	@Override
	public boolean saveBook(Shell shell) {

		try {
			Connection con = null;
			try {
				con = info.getConnection(true);
				bookStructure.checkSructure(con, info);
				bs.setData(con, info);

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

	// ************************************************************************

	@Override
	public List<BookSection> getSections(BookInfo book) {

		try {
			Connection con = null;
			try {
				con = book.getConnection(true);
				return bs.getSections(con);

			} finally {
				con.close();
			}

		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public List<BookSection> getChildren(BookInfo book, BookSection section) {
		try {
			Connection con = null;
			try {
				con = book.getConnection(true);
				return bs.getChildren(con, section.id);

			} finally {
				con.close();
			}

		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public BookSection getParent(BookInfo book, BookSection section) {
		try {
			Connection con = null;
			try {
				con = book.getConnection(true);
				return bs.getParent(con, section.id);

			} finally {
				con.close();
			}

		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public boolean hasChildren(BookInfo book, BookSection section) {
		try {
			Connection con = null;
			try {
				con = book.getConnection(true);
				return bs.hasChildren(con, section.id);

			} finally {
				con.close();
			}

		} catch (Exception e) {

		}
		return false;
	}

	// ************************************************************************

	@Override
	public void addBookSection(BookInfo book, BookSection section) {
		try {
			Connection con = null;
			try {
				con = book.getConnection(true);
				bs.addSection(con, section == null ? 0 : section.id);

			} finally {
				con.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW, null);
	}
}
