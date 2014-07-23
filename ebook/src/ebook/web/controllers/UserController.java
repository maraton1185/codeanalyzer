package ebook.web.controllers;

import java.util.List;

import ebook.core.App;
import ebook.module.userList.tree.UserInfo;

public class UserController {

	public final static String SessionAttributeName = "user";

	public static UserInfo get(String user, String password) {

		List<UserInfo> users = App.srv.us().find(user);

		for (UserInfo item : users) {

			if (item.getOptions().password.equalsIgnoreCase(password))
				return item;

		}

		return null;

	}
}
