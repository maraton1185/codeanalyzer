package ebook.module.confList.view;

import ebook.core.models.ModelObject;
import ebook.module.confList.tree.ListConfInfo;
import ebook.utils.Const;

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

	public String getPath() {
		return data.getDbPath().toString();
	}

	public String getdbFileName() {
		return data.getDbPath().lastSegment()
				.concat(Const.DEFAULT_DB_EXTENSION);
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
