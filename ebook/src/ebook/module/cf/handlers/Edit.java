package ebook.module.cf.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.dialogs.EditDialog;
import ebook.module.cf.interfaces.ICf;

public class Edit {

	@Execute
	public void execute(Shell shell, @Optional ICf db) {

		new EditDialog(shell, db).open();

	}

	@CanExecute
	public boolean canExecute(@Optional ICf db) {
		return db != null;
	}

}