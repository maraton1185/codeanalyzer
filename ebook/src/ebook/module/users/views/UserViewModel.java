package ebook.module.users.views;

import ebook.core.models.ModelObject;
import ebook.module.users.tree.UserInfo;

public class UserViewModel extends ModelObject {

	public UserInfo data;

	public UserViewModel(UserInfo data) {
		super();
		this.data = data;
	}

	public String getPassword() {
		return data.options.password;
	}

	public void setPassword(String value) {

		fireIndexedPropertyChange("password", this.data.options.password,
				this.data.options.password = value);
	}

	public String getTitle() {
		return data.getTitle();
	}

	public void setTitle(String value) {

		fireIndexedPropertyChange("title", data.getTitle(), value);
		data.setTitle(value);
	}

	public String getDescription() {
		return data.options.description;
	}

	public void setDescription(String value) {

		fireIndexedPropertyChange("description", data.options.description,
				data.options.description = value);
	}

	public UserInfo getData() {
		return data;
	}

	public boolean isGroup() {
		return !data.isGroup();
	}
}
