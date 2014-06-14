package ebook.module.userList.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import ebook.module.userList.IUserManager;
import ebook.module.userList.tree.UserInfo;

public class AddGroup {
	@Execute
	public void execute(Shell shell, IUserManager um, @Optional UserInfo user) {
		InputDialog dlg = new InputDialog(shell,
				ebook.utils.Strings.get("appTitle"),
				"¬ведите название роли:", "", null);
		if (dlg.open() == Window.OK)

		{
			UserInfo data = new UserInfo();
			data.setTitle(dlg.getValue());
			data.setGroup(true);
			// data.password = "";
			um.add(data, user, false, shell);

			// bm.add((ITreeItemInfo) data);
		}

	}

}