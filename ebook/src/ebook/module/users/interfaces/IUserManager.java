package ebook.module.users.interfaces;

import org.eclipse.swt.widgets.Shell;

import ebook.module.users.tree.UserInfo;
import ebook.module.users.tree.UserInfoSelection;

public interface IUserManager {

	void add(UserInfo data, UserInfo user, boolean sub, Shell shell);

	void delete(UserInfoSelection selection);

	boolean save(UserInfo data, Shell shell);

}
