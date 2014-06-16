package ebook.module.cf.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.cf.interfaces.ICf;
import ebook.module.cf.interfaces.ICfManager;
import ebook.utils.Events;
import ebook.utils.Strings;

public class DbExecute {

	@Execute
	public void execute(ICf db, ICfManager dbManager, Shell shell,
			ICfManager dbMng) {

		if (!MessageDialog.openConfirm(shell, Strings.get("appTitle"),
				db.getName() + ": \n" + dbMng.getOperationName(db.getType())
						+ "?")) {

			return;
		}

		dbManager.execute(db, shell);
		App.br.post(Events.EVENT_UPDATE_CONFIG_LIST, null);

	}

	@CanExecute
	public boolean canExecute(@Optional ICf db) {
		return db != null;
	}

}
