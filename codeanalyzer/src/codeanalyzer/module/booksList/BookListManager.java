package codeanalyzer.module.booksList;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.core.App;
import codeanalyzer.module.books.BookConnection;
import codeanalyzer.module.books.BookStructure;
import codeanalyzer.module.booksList.tree.ListBookInfo;
import codeanalyzer.module.booksList.tree.ListBookInfoOptions;
import codeanalyzer.module.booksList.tree.ListBookInfoSelection;
import codeanalyzer.utils.Events;
import codeanalyzer.utils.Strings;

public class BookListManager implements IBookListManager {

	BookStructure bookStructure = new BookStructure();

	BookListService bs = App.srv.bls();

	@Override
	public void addGroup(ListBookInfo data, ListBookInfo book, boolean sub)
			throws InvocationTargetException {

		bs.add(data, book, sub);

	}

	@Override
	public void addBook(String value, ListBookInfo parent)
			throws InvocationTargetException {

		BookConnection book = new BookConnection(value);

		ListBookInfo data = new ListBookInfo();
		data.setTitle(book.getName());
		data.setGroup(false);
		ListBookInfoOptions opt = new ListBookInfoOptions();
		opt.path = book.getFullName();
		data.options = opt;
		bs.add(data, parent, true);

	}

	@Override
	public void addBookToList(IPath path, ListBookInfo selected)
			throws InvocationTargetException {

		BookConnection book = new BookConnection(path);

		ListBookInfo data = new ListBookInfo();
		data.setTitle(book.getName());
		data.setGroup(false);
		ListBookInfoOptions opt = new ListBookInfoOptions();
		opt.path = book.getFullName();
		data.options = opt;
		bs.add(data, selected, true);

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

		bs.delete(selection);

	}

	@Override
	public boolean save(ListBookInfo data, Shell shell) {

		try {

			bs.saveOptions(data);

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
