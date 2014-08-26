package ebook.module.tree;

import org.eclipse.swt.graphics.Image;

import ebook.module.db.DbOptions;

public abstract class TreeItemInfo implements ITreeItemInfo {

	public TreeItemInfo(DbOptions options) {
		this.options = options;
	}

	// public TreeItemInfo(DbOptions options, TreeItemInfo info) {
	// this.options = options;
	// for (Field f : this.getClass().getDeclaredFields()) {
	// try {
	// f.set(this, f.get(info));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }

	@Override
	public Image getListImage() {
		return null;
	}

	@Override
	public void setListImage(Image img) {

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TreeItemInfo)
			return ((TreeItemInfo) obj).id.equals(id);
		else
			return super.equals(obj);
	}

	private DbOptions options;
	private String title;
	private boolean isGroup;
	private Integer id;
	private int parent;
	private int sort;

	@Override
	public int getSort() {
		return sort;
	}

	@Override
	public void setSort(int sort) {
		this.sort = sort;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;

	}

	@Override
	public void setACL() {

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
	public DbOptions getOptions() {
		return options;
	}

	@Override
	public void setOptions(DbOptions options) {
		this.options = options;
	}

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

	@Override
	public boolean isRoot() {
		return getId() == ITreeService.rootId;
	}

	@Override
	public void setRoot() {

	}

}
