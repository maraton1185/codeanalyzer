package codeanalyzer.module.tree;

import codeanalyzer.core.models.DbOptions;

public abstract class TreeItemInfo implements ITreeItemInfo {
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TreeItemInfo)
			return ((TreeItemInfo) obj).id.equals(id);
		else
			return super.equals(obj);
	}

	public String title;
	public boolean isGroup;
	public Integer id;
	public int parent;

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
	public String getSuffix() {
		return "";
	}

	@Override
	public Integer getParent() {
		return parent;
	}

	@Override
	public abstract DbOptions getOptions();

}
