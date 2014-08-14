package ebook.module.bookList.handlers;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.module.book.BookConnection;
import ebook.module.bookList.tree.ListBookInfo;
import ebook.utils.Strings;

public class Unlock {
	@Execute
	public void execute(ListBookInfo item, Shell shell) {
		try {
			BookConnection con = new BookConnection(item.getPath(), false);
			con.closeConnection();
			MessageDialog.openInformation(shell, Strings.title("appTitle"),
					"Файл книги разблокирован.");
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Ошибка операции. Перезайдите в программу.");
		}
	}

	@CanExecute
	public boolean canExecute(@Optional @Active ListBookInfo item) {
		return item != null && !item.isGroup();
	}

}