 
package codeanalyzer.handlers.main;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.dialogs.OptionsDialog;

public class OptionsHandler {
	@Execute
	public void execute(Shell shell) {
//		MessageDialog.openInformation(shell, "", "options");
		new OptionsDialog().open();
	}
		
}