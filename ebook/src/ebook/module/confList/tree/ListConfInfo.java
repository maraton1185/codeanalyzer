package ebook.module.confList.tree;

import ebook.module.tree.TreeItemInfo;

public class ListConfInfo extends TreeItemInfo {

	public ListConfInfo(ListConfInfoOptions options) {
		super(options);
	}

	public ListConfInfo() {
		super(null);
	}

	@Override
	public ListConfInfoOptions getOptions() {
		return (ListConfInfoOptions) super.getOptions();
	}

}
