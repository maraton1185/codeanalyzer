package ebook.module.userList;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.userList.tree.UserInfo;
import ebook.module.userList.tree.UserInfoSelection;
import ebook.utils.Strings;

public class UserManager implements IUserManager {

	UserService srv = App.srv.us();

	@Override
	public void add(UserInfo data, UserInfo user, boolean sub, Shell shell) {
		try {
			srv.add(data, user, sub);
		} catch (InvocationTargetException e) {

			if (data.isGroup())
				MessageDialog
						.openError(shell, Strings.get("appTitle"),
								"Ошибка создании группы. \nВозможно, группа с таким именем уже существует.");
			else
				MessageDialog
						.openError(
								shell,
								Strings.get("appTitle"),
								"Ошибка создании пользователя. \nВозможно, пользователь с таким именем уже существует.");
		}

	}

	@Override
	public void delete(UserInfoSelection selection) {
		int parent = selection.getParent();

		Iterator<ITreeItemInfo> iterator = selection.iterator();
		while (iterator.hasNext())
			srv.delete(iterator.next());

		if (parent != 0)
			srv.selectLast(parent);

	}

	@Override
	public boolean save(UserInfo data, Shell shell) {
		try {
			srv.saveOptions(data);
		} catch (InvocationTargetException e) {
			MessageDialog
					.openError(
							shell,
							Strings.get("appTitle"),
							"Ошибка сохранения пользователя. \nВозможно, пользователь с таким именем уже существует.");

			return false;
		}

		return true;
	}

}
