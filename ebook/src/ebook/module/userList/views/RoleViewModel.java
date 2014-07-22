package ebook.module.userList.views;

import ebook.core.models.ModelObject;

public class RoleViewModel extends ModelObject {

	public String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String value) {
		fireIndexedPropertyChange("title", this.title, this.title = value);
	}

}
