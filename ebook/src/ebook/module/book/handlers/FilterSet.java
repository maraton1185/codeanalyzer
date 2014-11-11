package ebook.module.book.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.utils.Strings;

public class FilterSet {
	@Execute
	public void execute(@Optional SectionInfo item, Shell shell,
			@Active BookConnection book) {
		if (item == null)
			return;
		if (!item.isGroup()) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Фильтр можно устанавливать только на группу.");
			return;
		}
		try {
			book.srv().setRoot(item);
		} catch (Exception e) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Ошибка установки фильтра.");
		}
	}
}