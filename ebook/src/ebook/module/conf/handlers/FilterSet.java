package ebook.module.conf.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.module.conf.ConfConnection;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ListInfo;
import ebook.utils.Events;
import ebook.utils.Strings;

public class FilterSet {

	@Execute
	public void execute(Shell shell, @Optional ContextInfo item,
			@Active ConfConnection con,
			@Active @Named(Events.CONTEXT_ACTIVE_LIST) ListInfo list) {

		if (item == null)
			return;
		if (!item.isGroup()) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Фильтр можно устанавливать только на группу.");
			return;
		}
		try {
			con.srv(list).setRoot(item);
		} catch (Exception e) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Ошибка установки фильтра.");
		}
	}
}