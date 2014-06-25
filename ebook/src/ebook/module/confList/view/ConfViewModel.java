package ebook.module.confList.view;

import ebook.core.models.ModelObject;
import ebook.module.confList.tree.ListConfInfo;

public class ConfViewModel extends ModelObject {

	public ListConfInfo data;

	public String description = "";

	public ConfViewModel(ListConfInfo data) {
		super();
		this.data = data;
	}

	public ListConfInfo getData() {
		return data;
	}

	public String getdbPath() {
		return data.getDbPath();
	}

	public String getdbFileName() {
		return data.getDbName();
	}

	public String getloadFolder() {
		return data.getLoadPath().toString();
	}

	public String getTitle() {
		return data.getTitle();
	}

	public boolean isGroup() {
		return data.isGroup();
	}

	public boolean isItem() {
		return !data.isGroup();
	}

	public String getDescription() {
		return data.getOptions().description;
	}

	public void setDescription(String value) {

		fireIndexedPropertyChange("description", data.getOptions().description,
				data.getOptions().description = value);
	}

}
