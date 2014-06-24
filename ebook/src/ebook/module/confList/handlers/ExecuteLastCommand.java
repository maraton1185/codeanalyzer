package ebook.module.confList.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.confList.tree.ListConfInfo;
import ebook.utils.Events;
import ebook.utils.Strings;

public class ExecuteLastCommand {

	@Execute
	public void execute(ListConfInfo item, Shell shell) {
		if (!MessageDialog.openConfirm(
				shell,
				Strings.get("appTitle"),
				item.getName() + ": \n"
						+ App.mng.cm().getOperationName(item.getType()) + "?")) {

			return;
		}

		App.mng.cm().execute(item, shell);
		App.br.post(Events.EVENT_UPDATE_CONFIG_LIST, null);
	}

	@CanExecute
	public boolean canExecute(@Optional @Active ListConfInfo item) {
		return item != null && !item.isGroup();
	}

}