package codeanalyzer.module.cf.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.dialogs.EditDialog;

public class Add {

	@Execute
	public void execute(Shell shell) {

		new EditDialog(shell, null).open();

	}

}