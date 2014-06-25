package ebook.module.confList.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.confList.tree.ListConfInfo;

public class Show {
	@Execute
	public void execute(ListConfInfo item, Shell shell) {
		App.mng.clm().open(item.getDbFullPath(), shell);
	}

	@CanExecute
	public boolean canExecute(@Optional ListConfInfo item) {
		return item != null && !item.isGroup();
	}

}