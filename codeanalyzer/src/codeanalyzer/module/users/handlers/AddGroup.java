 
package codeanalyzer.module.users.handlers;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.module.users.UserInfo;
import codeanalyzer.module.users.interfaces.IUserManager;
import codeanalyzer.utils.Strings;

public class AddGroup {
	@Execute
	public void execute(Shell shell, IUserManager um, @Optional UserInfo user) {
		InputDialog dlg = new InputDialog(shell,
				codeanalyzer.utils.Strings.get("appTitle"),
				"Введите название роли:", "", null);
		if (dlg.open() == Window.OK)
			try {

				UserInfo data = new UserInfo();
				data.title = dlg.getValue();
				data.isGroup = true;
				// data.password = "";
				um.add(data, user, false);

				// bm.add((ITreeItemInfo) data);

			} catch (InvocationTargetException e) {
				MessageDialog.openError(shell, Strings.get("appTitle"),
								"Ошибка создании группы. \nВозможно, группа с таким именем уже существует.");
			}

	}

		
}