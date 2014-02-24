 
package codeanalyzer.handlers;

import org.eclipse.e4.core.di.annotations.Execute;

import codeanalyzer.dialogs.EditDialog;

public class EditConfigList {
	
	@Execute
	public void execute(EditDialog dlg) {
		
		dlg.open();
		
	}
		
}