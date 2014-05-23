package codeanalyzer.module.users;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.module.tree.ITreeItemInfo;
import codeanalyzer.module.users.interfaces.IUserManager;
import codeanalyzer.utils.Strings;

public class UserManager implements IUserManager {

	UserService us = new UserService();

	@Override
	public void add(UserInfo data, UserInfo user, boolean sub, Shell shell) {
		try {
			us.add(data, user, sub);
		} catch (InvocationTargetException e) {

			if (data.isGroup)
				MessageDialog
						.openError(shell, Strings.get("appTitle"),
								"������ �������� ������. \n��������, ������ � ����� ������ ��� ����������.");
			else
				MessageDialog
						.openError(
								shell,
								Strings.get("appTitle"),
								"������ �������� ������������. \n��������, ������������ � ����� ������ ��� ����������.");
		}

	}

	@Override
	public void delete(UserInfoSelection selection) {
		int parent = selection.getParent();

		Iterator<ITreeItemInfo> iterator = selection.iterator();
		while (iterator.hasNext())
			us.delete(iterator.next());

		if (parent != 0)
			us.selectLast(parent);

	}

	@Override
	public boolean save(UserInfo data, Shell shell) {
		try {
			us.saveOptions(data);
		} catch (InvocationTargetException e) {
			MessageDialog
					.openError(
							shell,
							Strings.get("appTitle"),
							"������ ���������� ������������. \n��������, ������������ � ����� ������ ��� ����������.");

			return false;
		}

		return true;
	}

}
