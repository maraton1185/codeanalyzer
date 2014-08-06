package ebook.module.conf.tree;

import ebook.module.tree.TreeItemInfo;

public class ListInfo extends TreeItemInfo {

	public ListInfo(ContextInfoOptions options) {
		super(options);
	}

	public ListInfo() {
		super(null);
	}

	@Override
	public ListInfoOptions getOptions() {
		return (ListInfoOptions) super.getOptions();
	}

	@Override
	public boolean isTitleIncrement() {
		return true;
	}

}
