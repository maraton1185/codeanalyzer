 
package codeanalyzer.handlers;

import org.eclipse.e4.core.di.annotations.Execute;

import codeanalyzer.dialogs.EditDialog;

public class AddToConfigList {
	
	@Execute
	public void execute(EditDialog dlg) {
		
		dlg.open();
		
	}
		
}