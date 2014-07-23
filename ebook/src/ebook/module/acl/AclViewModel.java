package ebook.module.acl;

import ebook.core.models.ModelObject;

public class AclViewModel extends ModelObject {

	private Integer id;

	public Integer getId() {
		return id;
	}

	private String title;

	public AclViewModel(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AclViewModel)
			return ((AclViewModel) obj).id.equals(id);
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
