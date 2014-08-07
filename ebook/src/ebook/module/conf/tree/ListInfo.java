package ebook.module.conf.tree;

import ebook.module.tree.TreeItemInfo;

public class ListInfo extends TreeItemInfo {

	public ListInfo(ListInfoOptions options) {
		super(options);
	}

	@Override
	public ListInfoOptions getOptions() {

		return (ListInfoOptions) super.getOptions();
	}

	public ListInfo() {
		super(null);
	}

	@Override
	public boolean isTitleIncrement() {
		return true;
	}

}
