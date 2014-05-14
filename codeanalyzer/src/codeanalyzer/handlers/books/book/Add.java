package codeanalyzer.handlers.books.book;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.books.interfaces.IBookManager;
import codeanalyzer.cf.interfaces.ICf;
import codeanalyzer.core.db.model.BookInfo;
import codeanalyzer.utils.Strings;

public class Add {
	@Execute
	public void execute(@Optional ICf db, Shell shell, IBookManager bm,
			@Optional BookInfo book) {

		InputDialog dlg = new InputDialog(shell,
				codeanalyzer.utils.Strings.get("appTitle"),
				"Введите название книги:", db == null ? "" : db.getName()
						.replaceAll("[//\\:\\.]", "_"), null);
		if (dlg.open() == Window.OK) {
			try {
				bm.addBook(dlg.getValue(), book);
			} catch (InvocationTargetException e) {
				MessageDialog
						.openError(shell, Strings.get("appTitle"),
								"Ошибка создании книги. \nВозможно, книга с таким названием уже существует.");
			}
		}

		// MessageDialog.openInformation(shell, "", "создать книгу");

	}
	// @CanExecute
	// public boolean canExecute(@Optional @Named(Const.CONTEXT_SELECTED_DB) IDb
	// db) {
	// return db != null;
	// }
}