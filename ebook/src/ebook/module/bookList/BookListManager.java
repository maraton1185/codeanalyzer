package ebook.module.bookList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
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
import ebook.module.tree.item.ITreeItemInfo;
import ebook.module.tree.manager.TreeManager;
import ebook.utils.Events;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class BookListManager extends TreeManager {

	// BookStructure bookStructure = new BookStructure();

	public BookListManager() {
		super(App.srv.bl());
	}

	// BookListService srv = App.srv.bls();

	@Override
	public void add(ITreeItemInfo parent, Shell shell) {

	}

	public void add(ListConfInfo db, ListBookInfo parent, Shell shell) {

		// if (!pico.get(IAuthorize.class).checkBooksCount(shell))
		// return;

		InputDialog dlg = new InputDialog(shell,
				ebook.utils.Strings.title("appTitle"),
				"Введите имя файла книги:",
				ebook.utils.Strings.value("bookFileName"), null);
		if (dlg.open() == Window.OK) {
			try {

				BookConnection con = new BookConnection(dlg.getValue());

				ListBookInfoOptions opt = new ListBookInfoOptions();
				// opt.path = con.getFullName();

				ListBookInfo data = new ListBookInfo(opt);
				data.setTitle(con.getTitle());
				data.setGroup(false);
				data.setPath(con.getName());
				srv.add(data, parent, true);

			} catch (InvocationTargetException e) {
				MessageDialog
						.openError(shell, Strings.title("appTitle"),
								"Ошибка создании книги. \nВозможно, книга с таким названием уже существует.");
			}
		}

	}

	@Override
	public void addGroup(ITreeItemInfo parent, Shell shell) {

		InputDialog dlg = new InputDialog(shell,
				ebook.utils.Strings.title("appTitle"),
				"Введите название группы:", "", null);
		if (dlg.open() == Window.OK)
			try {

				ListBookInfo data = new ListBookInfo();
				data.setTitle(dlg.getValue());
				data.setGroup(true);
				srv.add(data, parent, false);

			} catch (InvocationTargetException e) {
				MessageDialog.openError(shell, Strings.title("appTitle"),
						"Ошибка создания группы.");
			}

	}

	@Override
	public void addSubGroup(ITreeItemInfo parent, Shell shell) {

		InputDialog dlg = new InputDialog(shell,
				ebook.utils.Strings.title("appTitle"),
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
				MessageDialog.openError(shell, Strings.title("appTitle"),
						"Ошибка создания подгруппы.");
			}

	}

	@Override
	public void addToList(final ITreeItemInfo parent, final Shell shell) {

		// if (!pico.get(IAuthorize.class).checkBooksCount(shell))
		// return;

		final List<IPath> files = Utils.browseFileMulti(
				new Path(PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY)),
				shell, Strings.title("appTitle"), "*.db");
		if (files == null)
			return;

		BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
			@Override
			public void run() {
				for (IPath path : files) {
					try {

						BookConnection con = new BookConnection(path, true);

						ListBookInfoOptions opt = new ListBookInfoOptions();
						opt.description = readBookDescription(new Path(con
								.getFullName()));

						ListBookInfo data = new ListBookInfo(opt);
						data.setTitle(con.getTitle());
						data.setGroup(false);
						data.setPath(con.getName());
						// data.setImage(readBookImage(new
						// Path(con.getFullName())));
						srv.add(data, parent, true);

					} catch (InvocationTargetException e) {
						if (files.size() > 1
								&& !MessageDialog.openConfirm(
										shell,
										Strings.title("appTitle"),
										"Ошибка открытия книги: "
												+ path
												+ "\nВозможно, структура книги не соответствует ожидаемой."
												+ "\nПродолжить?"))
							return;
						else
							MessageDialog.openError(
									shell,
									Strings.title("appTitle"),
									"Ошибка открытия книги: "
											+ path
											+ "\nВозможно, структура книги не соответствует ожидаемой.");
					}
				}
			}
		});

	}

	private String readBookDescription(IPath path) {

		String description;
		try {
			File f = new File(path.addFileExtension("txt").toString());
			if (!f.exists())
				throw new FileNotFoundException();

			BufferedReader br = null;
			StringBuffer sb = new StringBuffer();
			try {
				Reader in = new InputStreamReader(new FileInputStream(f));
				br = new BufferedReader(in);
				String source_line = null;
				while ((source_line = br.readLine()) != null) {
					sb.append(source_line + '\n');
				}
			} finally {
				br.close();
			}
			description = sb.toString();

		} catch (Exception e) {
			description = "Нет описания";
		}

		return description;
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
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Ошибка сохранения книги.");

			return false;
		}

		return true;
	}

	public void open(IPath path, Shell shell) {

		if (path == null)
			return;

		try {

			BookConnection book = new BookConnection(path, true);
			// book.openConnection();
			App.ctx.set(BookConnection.class, book);
			App.br.post(Events.EVENT_SHOW_BOOK, null);

		} catch (Exception e) {

			App.ctx.set(BookConnection.class, null);
			if (shell != null)
				MessageDialog.openError(shell, Strings.title("appTitle"),
						"Ошибка открытия книги.");
		}
	}

}
