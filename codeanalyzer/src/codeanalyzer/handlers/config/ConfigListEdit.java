package codeanalyzer.handlers.config;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.core.interfaces.IDb;
import codeanalyzer.dialogs.EditDialog;

public class ConfigListEdit {

	@Execute
	public void execute(Shell shell, @Optional IDb db) {

		new EditDialog(shell, db).open();

	}

	@CanExecute
	public boolean canExecute(@Optional IDb db) {
		return db != null;
	}

}