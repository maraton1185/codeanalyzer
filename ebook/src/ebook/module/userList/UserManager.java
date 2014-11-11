package ebook.module.userList;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.module.tree.manager.TreeManager;
import ebook.module.userList.tree.UserInfo;
import ebook.utils.Strings;

public class UserManager extends TreeManager {

	public UserManager() {
		super(App.srv.us());
	}

	@Override
	public void add(ITreeItemInfo parent, Shell shell) {

		// if (!pico.get(IAuthorize.class).checkUsersCount(shell))
		// return;

		InputDialog dlg = new InputDialog(shell,
				ebook.utils.Strings.title("appTitle"),
				"Введите имя пользователя:", "", null);
		if (dlg.open() == Window.OK) {

			UserInfo data = new UserInfo();
			data.setTitle(dlg.getValue());
			data.setGroup(false);
			// data.password = "";
			// um.add(data, user, true, shell);

			try {
				srv.add(data, parent, true);
			} catch (InvocationTargetException e) {

				MessageDialog
						.openError(
								shell,
								Strings.title("appTitle"),
								"Ошибка создания пользователя. \nВозможно, пользователь с таким именем уже существует.");
			}

		}
	}

	@Override
	public boolean save(ITreeItemInfo data, Shell shell) {
		try {
			srv.saveOptions(data);
		} catch (InvocationTargetException e) {
			MessageDialog
					.openError(
							shell,
							Strings.title("appTitle"),
							"Ошибка сохранения пользователя. \nВозможно, пользователь с таким именем уже существует.");

			return false;
		}

		return true;
	}

	@Override
	public void addGroup(ITreeItemInfo parent, Shell shell) {
		InputDialog dlg = new InputDialog(shell,
				ebook.utils.Strings.title("appTitle"),
				"Введите название роли:", "", null);
		if (dlg.open() == Window.OK)

		{
			UserInfo data = new UserInfo();
			data.setTitle(dlg.getValue());
			data.setGroup(true);
			// data.password = "";
			// um.add(data, user, false, shell);
			try {
				srv.add(data, parent, false);
			} catch (InvocationTargetException e) {

				MessageDialog
						.openError(shell, Strings.title("appTitle"),
								"Ошибка создания роли. \nВозможно, роль с таким именем уже существует.");
			}
			// bm.add((ITreeItemInfo) data);
		}

	}

	@Override
	public void addSubGroup(ITreeItemInfo parent, Shell shell) {
		InputDialog dlg = new InputDialog(shell,
				ebook.utils.Strings.title("appTitle"),
				"Введите название роли:", "", null);
		if (dlg.open() == Window.OK)

		{
			UserInfo data = new UserInfo();
			data.setTitle(dlg.getValue());
			data.setGroup(true);
			// um.add(data, user, true, shell);

			try {
				srv.add(data, parent, true);
			} catch (InvocationTargetException e) {

				MessageDialog
						.openError(shell, Strings.title("appTitle"),
								"Ошибка создания роли. \nВозможно, роль с таким именем уже существует.");
			}
		}

	}

}
