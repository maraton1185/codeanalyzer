package codeanalyzer.module.users;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import codeanalyzer.module.tree.ITreeItemInfo;
import codeanalyzer.module.users.interfaces.IUserManager;

public class UserManager implements IUserManager {

	UserService us = new UserService();

	@Override
	public void add(UserInfo data, UserInfo user, boolean sub)
			throws InvocationTargetException {
		us.add(data, user, sub);

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

}
