package ebook.module.confList.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.confList.tree.ListConfInfo;
import ebook.utils.Strings;

public class FilterSet {
	@Execute
	public void execute(@Optional ListConfInfo item, Shell shell) {
		if (item == null)
			return;
		if (!item.isGroup()) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Фильтр можно устанавливать только на группу.");
			return;
		}
		try {
			App.srv.cl().setRoot(item);
		} catch (Exception e) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Ошибка установки фильтра.");
		}
	}
}