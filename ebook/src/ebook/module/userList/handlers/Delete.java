package ebook.module.userList.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.cf.interfaces.ICf;
import ebook.module.tree.ITreeItemSelection;

public class Delete {
	@Execute
	public void execute(@Optional ICf db, Shell shell,
			@Named("userListSelection") ITreeItemSelection selection) {

		App.mng.um().delete(selection, shell);
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Active @Named("userListSelection") ITreeItemSelection selection) {
		return selection != null;
	}

}