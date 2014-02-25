 
package codeanalyzer.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.dialogs.EditDialog;

public class ConfigListAdd {
	
	@Execute
	public void execute(Shell shell, IEventBroker br) {
		
		new EditDialog(shell, null, br).open();
		
	}
		
}