 
package ebook.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import ebook.dialogs.OptionsDialog;

public class Options {
	@Execute
	public void execute(Shell shell) {
//		MessageDialog.openInformation(shell, "", "options");
		new OptionsDialog().open();
	}
		
}