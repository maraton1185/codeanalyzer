package ebook.module.bookList;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.bookList.tree.ListBookInfo;
import ebook.module.bookList.tree.ListBookInfoOptions;
import ebook.module.confList.tree.ListConfInfo;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.TreeManager;
import ebook.utils.Events;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class BookListManager extends TreeManager {

	// BookStructure bookStructure = new BookStructure();

	public BookListManager() {
		super(App.srv.bls());
	}

	// BookListService srv = App.srv.bls();

	@Override
	public void add(ITreeItemInfo parent, Shell shell) {

	}

	public void add(ListConfInfo db, ListBookInfo parent, Shell shell) {
		InputDialog dlg = new InputDialog(shell,
				ebook.utils.Strings.get("appTitle"),
				"Введите имя файла книги:", db == null ? "" : db.getTitle()
						.replaceAll("[//\\:\\.]", "_"), null);
		if (dlg.open() == Window.OK) {
			try {

				BookConnection con = new BookConnection(dlg.getValue());

				ListBookInfoOptions opt = new ListBookInfoOptions();
				opt.path = con.getFullName();

				ListBookInfo data = new ListBookInfo(opt);
				data.setTitle(con.getName());
				data.setGroup(false);
				srv.add(data, parent, true);

			} catch (InvocationTargetException e) {
				MessageDialog
						.openError(shell, Strings.get("appTitle"),
								"Ошибка создании книги. \nВозможно, книга с таким названием уже существует.");
			}
		}

	}

	@Override
	public void addGroup(ITreeItemInfo parent, Shell shell) {

		InputDialog dlg = new InputDialog(shell,
				ebook.utils.Strings.get("appTitle"),
				"Введите название группы:", "", null);
		if (dlg.open() == Window.OK)
			try {

				ListBookInfo data = new ListBookInfo();
				data.setTitle(dlg.getValue());
				data.setGroup(true);
				srv.add(data, parent, false);

			} catch (InvocationTargetException e) {
				MessageDialog.openError(shell, Strings.get("appTitle"),
						"Ошибка создания группы.");
			}

	}

	@Override
	public void addSubGroup(ITreeItemInfo parent, Shell shell) {

		InputDialog dlg = new InputDialog(shell,
				ebook.utils.Strings.get("appTitle"),
				"Введите название подгруппы:", "", null);
		if (dlg.open() == Window.OK)
			try {

				ListBookInfo data = new ListBookInfo();
				data.setTitle(dlg.getValue());
				data.setGroup(true);
				// data.path = "";
				srv.add(data, parent, true);
				// ((ITreeService) bm).add(data, book, true);

				// bm.addBooksGroup(dlg.getValue(), book, true);
			} catch (InvocationTargetException e) {
				MessageDialog.openError(shell, Strings.get("appTitle"),
						"Ошибка создания подгруппы.");
			}

	}

	@Override
	public void addToList(final ITreeItemInfo parent, final Shell shell) {
		final List<IPath> files = Utils.browseFileMulti(
				new Path(PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY)),
				shell, Strings.get("appTitle"), "*.db");
		if (files == null)
			return;

		BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
			@Override
			public void run() {
				for (IPath path : files) {
					try {

						BookConnection con = new BookConnection(path);

						ListBookInfoOptions opt = new ListBookInfoOptions();
						opt.path = con.getFullName();
						ListBookInfo data = new ListBookInfo(opt);
						data.setTitle(con.getName());
						data.setGroup(false);
						srv.add(data, parent, true);

					} catch (InvocationTargetException e) {
						if (files.size() > 1
								&& !MessageDialog.openConfirm(
										shell,
										Strings.get("appTitle"),
										"Ошибка открытия книги: "
												+ path
												+ "\nВозможно, структура книги не соответствует ожидаемой."
												+ "\nПродолжить?"))

							return;
					}
				}
			}
		});

	}

	@Override
	public boolean save(ITreeItemInfo data, Shell shell) {

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

	public void open(IPath path, Shell shell) {

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

}
