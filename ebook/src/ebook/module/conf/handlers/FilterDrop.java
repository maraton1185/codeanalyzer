package ebook.module.conf.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.module.conf.ConfConnection;
import ebook.module.conf.tree.ListInfo;
import ebook.utils.Events;
import ebook.utils.Strings;

public class FilterDrop {

	@Execute
	public void execute(Shell shell, @Active ConfConnection con,
			@Active @Named(Events.CONTEXT_ACTIVE_LIST) ListInfo list) {

		try {
			con.srv(list).dropRoot();
		} catch (Exception e) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Ошибка снятия фильтра.");
		}

	}

}