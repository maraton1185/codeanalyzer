package ebook.module.bookList.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.bookList.tree.ListBookInfo;
import ebook.utils.Strings;

public class FilterSet {
	@Execute
	public void execute(@Optional ListBookInfo book, Shell shell) {
		if (book == null)
			return;
		if (!book.isGroup()) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Фильтр можно устанавливать только на группу.");
			return;
		}
		try {
			App.srv.bl().setRoot(book);
		} catch (Exception e) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Ошибка установки фильтра.");
		}
	}
}