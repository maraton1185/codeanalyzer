package ebook.module.userList.views;

import ebook.core.models.ModelObject;

public class RoleViewModel extends ModelObject {

	public Integer id;

	public String title;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RoleViewModel)
			return ((RoleViewModel) obj).title.equalsIgnoreCase(title);
		else
			return super.equals(obj);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String value) {
		fireIndexedPropertyChange("title", this.title, this.title = value);
	}

}
