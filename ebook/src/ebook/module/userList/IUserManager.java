package ebook.module.userList;

import org.eclipse.swt.widgets.Shell;

import ebook.module.userList.tree.UserInfo;
import ebook.module.userList.tree.UserInfoSelection;

public interface IUserManager {

	void add(UserInfo data, UserInfo user, boolean sub, Shell shell);

	void delete(UserInfoSelection selection);

	boolean save(UserInfo data, Shell shell);

}
