package ebook.module.userList.views;

import ebook.core.models.ModelObject;
import ebook.module.userList.tree.UserInfo;

public class UserViewModel extends ModelObject {

	public UserInfo data;

	public UserViewModel(UserInfo data) {
		super();
		this.data = data;
	}

	public String getPassword() {
		return data.getOptions().password;
	}

	public void setPassword(String value) {

		fireIndexedPropertyChange("password", data.getOptions().password,
				data.getOptions().password = value);
	}

	public String getTitle() {
		return data.getTitle();
	}

	public void setTitle(String value) {

		fireIndexedPropertyChange("title", data.getTitle(), value);
		data.setTitle(value);
	}

	public String getDescription() {
		return data.getOptions().description;
	}

	public void setDescription(String value) {

		fireIndexedPropertyChange("description", data.getOptions().description,
				data.getOptions().description = value);
	}

	public UserInfo getData() {
		return data;
	}

	public boolean isGroup() {
		return !data.isGroup();
	}
}
