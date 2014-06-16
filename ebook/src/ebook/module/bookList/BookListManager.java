package ebook.module.bookList;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.bookList.tree.ListBookInfo;
import ebook.module.bookList.tree.ListBookInfoOptions;
import ebook.module.bookList.tree.ListBookInfoSelection;
import ebook.utils.Events;
import ebook.utils.Strings;

public class BookListManager implements IBookListManager {

	// BookStructure bookStructure = new BookStructure();

	BookListService srv = App.srv.bls();

	@Override
	public void add(String value, ListBookInfo parent)
			throws InvocationTargetException {

		BookConnection con = new BookConnection(value);

		ListBookInfoOptions opt = new ListBookInfoOptions();
		opt.path = con.getFullName();

		ListBookInfo data = new ListBookInfo(opt);
		data.setTitle(con.getName());
		data.setGroup(false);
		srv.add(data, parent, true);

	}

	@Override
	public void addGroup(ListBookInfo data, ListBookInfo book, boolean sub)
			throws InvocationTargetException {

		srv.add(data, book, sub);

	}

	@Override
	public void addToList(IPath path, ListBookInfo selected)
			throws InvocationTargetException {

		BookConnection con = new BookConnection(path);

		ListBookInfoOptions opt = new ListBookInfoOptions();
		opt.path = con.getFullName();
		ListBookInfo data = new ListBookInfo(opt);
		data.setTitle(con.getName());
		data.setGroup(false);
		srv.add(data, selected, true);

	}

	@Override
	public void openBook(IPath path, Shell shell) {

		if (path == null)
			return;

		try {

			BookConnection book = new BookConnection(path);
			book.openConnection();
			App.ctx.set(BookConnection.class, book);
			App.br.post(Events.EVENT_SHOW_BOOK, null);

		} catch (Exception e) {

			App.ctx.set(BookConnection.class, null);
			if (shell != null)
				MessageDialog.openError(shell, Strings.get("appTitle"),
						"Ошибка открытия книги.");
		}
	}

	@Override
	public void delete(ListBookInfoSelection selection) {

		srv.delete(selection);

	}

	@Override
	public boolean save(ListBookInfo data, Shell shell) {

		try {

			srv.saveOptions(data);

			// if (!isGroup) {
			// BookConnection book = new BookConnection(data.getPath());
			// book.service().setBookInfo(info);
			// }

		} catch (InvocationTargetException e) {
			MessageDialog.openError(shell, Strings.get("appTitle"),
					"Ошибка сохранения книги.");

			return false;
		}

		return true;
	}

}
