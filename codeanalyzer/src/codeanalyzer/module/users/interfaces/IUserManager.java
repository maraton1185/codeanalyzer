package codeanalyzer.module.users.interfaces;

import java.lang.reflect.InvocationTargetException;

import codeanalyzer.module.users.UserInfo;
import codeanalyzer.module.users.UserInfoSelection;

public interface IUserManager {

	void add(UserInfo data, UserInfo user, boolean sub)
			throws InvocationTargetException;

	void delete(UserInfoSelection selection);

}
