 
package codeanalyzer.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.dialogs.EditDialog;

public class ConfigListAdd {
	
	@Execute
	public void execute(Shell shell, IEventBroker br, EModelService model, MApplication application) {
		
		new EditDialog(shell, null, br, model, application).open();
		
	}
		
}