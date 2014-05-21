package codeanalyzer.module.users.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.module.users.UserInfo;
import codeanalyzer.module.users.interfaces.IUserManager;

public class Add {
	@Execute
	public void execute(Shell shell, IUserManager um, @Optional UserInfo user) {

		InputDialog dlg = new InputDialog(shell,
				codeanalyzer.utils.Strings.get("appTitle"),
				"¬ведите им€ пользовател€:", "", null);
		if (dlg.open() == Window.OK) {

			UserInfo data = new UserInfo();
			data.title = dlg.getValue();
			data.isGroup = false;
			// data.password = "";
			um.add(data, user, true, shell);

		}

	}

}