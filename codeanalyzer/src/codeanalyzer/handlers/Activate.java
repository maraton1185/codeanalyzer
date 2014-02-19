 
package codeanalyzer.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import dialogs.ActivateDialog;

public class Activate {
	@Execute
	public void execute(Shell shell, ActivateDialog dlg) {
		dlg.open();
	}
		
}