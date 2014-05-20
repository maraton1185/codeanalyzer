package codeanalyzer.module.books.list;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.Iterator;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.core.AppManager;
import codeanalyzer.core.Events;
import codeanalyzer.module.books.BookStructure;
import codeanalyzer.module.books.interfaces.IBookManager;
import codeanalyzer.module.tree.ITreeItemInfo;
import codeanalyzer.utils.Strings;

public class BookManager implements IBookManager {

	// IDbService dbManager = pico.get(IDbService.class);
	BookStructure bookStructure = new BookStructure();
	BookService bs = new BookService();
	BookService books;

	@Override
	public void addGroup(BookInfo data, BookInfo book, boolean sub)
			throws InvocationTargetException {

		bs.add(data, book, sub);

	}

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

				BookInfo data = new BookInfo();
				data.title = book.getName();
				data.isGroup = false;
				data.path = book.getFullName();
				bs.add(data, parent, true);

				AppManager.ctx.set(CurrentBookInfo.class, book);
				AppManager.br.post(Events.EVENT_UPDATE_BOOK_INFO, null);
			} finally {
				con.close();
			}
		} catch (Exception e) {
			throw new InvocationTargetException(e);
		}

	}

	@Override
	public void addBookToList(IPath path, BookInfo selected)
			throws InvocationTargetException {

		CurrentBookInfo book = new CurrentBookInfo();

		book.setPath(path);

		try {
			Connection con = null;
			try {
				con = book.makeConnection(true);
				bookStructure.checkSructure(con, book);
				bs.getData(con, book);
				book.setOpened(true);

				BookInfo data = new BookInfo();
				data.title = book.getName();
				data.isGroup = false;
				data.path = book.getFullName();
				bs.add(data, selected, true);

				AppManager.ctx.set(CurrentBookInfo.class, book);
				AppManager.br.post(Events.EVENT_UPDATE_BOOK_INFO, null);
			} finally {
				con.close();
			}
		} catch (Exception e) {

			throw new InvocationTargetException(e);
		}
	}

	@Override
	public void openBook(IPath path, Shell shell) {

		if (path == null)
			return;

		CurrentBookInfo book = new CurrentBookInfo();

		book.setPath(path);

		openBook(book, shell);

	}

	@Override
	public void openBook(CurrentBookInfo book, Shell shell) {

		if (book.isGroup)
			return;

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
			AppManager.br.post(Events.EVENT_SHOW_BOOK, null);

		} catch (Exception e) {

			AppManager.ctx.set(CurrentBookInfo.class, null);
			if (shell != null)
				MessageDialog.openError(shell, Strings.get("appTitle"),
						"Ошибка открытия книги.");
		}
		// AppManager.br.post(Const.EVENT_UPDATE_BOOK_INFO, null);
		// AppManager.br.post(Const.EVENT_SHOW_BOOK, null);
	}

	@Override
	public void delete(BookInfoSelection selection) {

		int parent = selection.getParent();

		Iterator<ITreeItemInfo> iterator = selection.iterator();
		while (iterator.hasNext())
			bs.delete(iterator.next());

		// for (BookInfo book : selection.list) {
		// bs.delete(book);
		// }
		if (parent != 0)
			bs.selectLast(parent);
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
