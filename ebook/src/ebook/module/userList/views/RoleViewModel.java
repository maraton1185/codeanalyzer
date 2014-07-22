package ebook.module.userList.views;

import ebook.core.models.ModelObject;

public class RoleViewModel extends ModelObject {

	private Integer id;

	public Integer getId() {
		return id;
	}

	private String title;

	public RoleViewModel(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RoleViewModel)
			return ((RoleViewModel) obj).id.equals(id);
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
