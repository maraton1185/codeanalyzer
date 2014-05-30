package ebook.module.tree;

import ebook.core.models.DbOptions;

public abstract class TreeItemInfo implements ITreeItemInfo {
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TreeItemInfo)
			return ((TreeItemInfo) obj).id.equals(id);
		else
			return super.equals(obj);
	}

	private String title;
	private boolean isGroup;
	private Integer id;
	private int parent;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;

	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public boolean isGroup() {
		return isGroup;
	}

	@Override
	public void setGroup(boolean value) {
		this.isGroup = value;
	}

	@Override
	public String getSuffix() {
		return "";
	}

	@Override
	public Integer getParent() {
		return parent;
	}

	@Override
	public abstract DbOptions getOptions();

	@Override
	public void setParent(int value) {
		this.parent = value;
	}

	@Override
	public void setId(int value) {
		this.id = value;

	}

	@Override
	public boolean isTitleIncrement() {
		return false;
	}

}
