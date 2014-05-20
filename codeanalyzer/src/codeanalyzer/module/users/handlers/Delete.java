 
package codeanalyzer.module.users.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.module.cf.interfaces.ICf;
import codeanalyzer.module.users.UserInfoSelection;
import codeanalyzer.module.users.interfaces.IUserManager;

public class Delete {
	@Execute
	public void execute(@Optional ICf db, Shell shell, IUserManager um,
			UserInfoSelection selection) {

		um.delete(selection);
	}

	@CanExecute
	public boolean canExecute(@Optional @Active UserInfoSelection selection) {
		return selection != null;
	}
		
}