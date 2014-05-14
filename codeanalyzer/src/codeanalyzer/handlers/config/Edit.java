package codeanalyzer.handlers.config;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.cf.interfaces.ICf;
import codeanalyzer.dialogs.EditDialog;

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