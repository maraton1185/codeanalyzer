package ebook.module.userList.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.userList.tree.UserInfo;

public class AddSubGroup {
	@Execute
	public void execute(Shell shell, @Optional UserInfo user) {

		App.mng.um().addSubGroup(user, shell);

	}

}