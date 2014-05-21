package codeanalyzer.module.users.views;

import codeanalyzer.core.models.ModelObject;
import codeanalyzer.module.users.UserInfo;

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
		return data.title;
	}

	public void setTitle(String value) {

		fireIndexedPropertyChange("title", data.title, data.title = value);
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
		return !data.isGroup;
	}
}
