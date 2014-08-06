package ebook.module.conf.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.tree.ListInfo;

public class ListAddGroup {

	@Execute
	public void execute(Shell shell, @Optional ListInfo item,
			@Active ConfConnection con) {

		App.mng.clm(con).addGroup(item, shell);

	}

	@CanExecute
	public boolean canExecute(@Optional @Active ListInfo item) {
		return item != null;
	}
}
