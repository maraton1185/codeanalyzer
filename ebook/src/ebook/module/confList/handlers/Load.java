package ebook.module.confList.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.module.conf.EditDialog;
import ebook.module.confList.tree.ListConfInfo;

public class Load {
	@Execute
	public void execute(ListConfInfo item, Shell shell) {
		new EditDialog(shell, item).open();
	}

	@CanExecute
	public boolean canExecute(@Optional @Active ListConfInfo item) {
		return item != null;
	}

}