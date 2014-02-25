 
package codeanalyzer.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.core.interfaces.IDb;
import codeanalyzer.utils.Const;

public class ConfigListExecute {
	@Execute
	public void execute(Shell shell) {
		MessageDialog.openInformation(shell, "", "execute");
	}
	
	@CanExecute
	public boolean canExecute(@Optional @Named(Const.CONTEXT_SELECTED_DB) IDb db) {
		return db!=null;
	}
		
}