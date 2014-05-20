package codeanalyzer.handlers.books.book;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.books.book.BookInfo;
import codeanalyzer.books.interfaces.IBookManager;
import codeanalyzer.cf.interfaces.ICf;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class AddExisted {
	@Execute
	public void execute(@Optional ICf db, final Shell shell,
			final IBookManager bm, @Optional final BookInfo book) {

		final List<IPath> files = Utils.browseFileMulti(
				new Path(PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY)),
				shell, Strings.get("appTitle"), "*.db");
		if (files == null)
			return;

		BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
			@Override
			public void run() {
				for (IPath f : files) {
					try {

						bm.addBookToList(f, book);

					} catch (InvocationTargetException e) {
						if (!MessageDialog.openConfirm(
								shell,
								Strings.get("appTitle"),
								"Ошибка открытия книги: "
										+ f
										+ "\nВозможно, структура книги не соответствует ожидаемой."
										+ "\nПродолжить?"))
							;
						return;
					}
				}
			}
		});

		// System.out.println(path.toString());
		// field.setText(path.toString());
		//
		// Text field = new Text(null, 0);
		// Utils.browseFile(field, shell);
		// InputDialog dlg = new InputDialog(shell,
		// codeanalyzer.utils.Strings.get("appTitle"),
		// "Введите имя файла книги:", db == null ? "" : db.getName()
		// .replaceAll("[//\\:\\.]", "_"), null);
		// if (dlg.open() == Window.OK) {
		// try {
		// bm.addBook(dlg.getValue(), book);
		// } catch (InvocationTargetException e) {
		// MessageDialog
		// .openError(shell, Strings.get("appTitle"),
		// "Ошибка создании книги. \nВозможно, книга с таким названием уже существует.");
		// }
		// }

	}
}