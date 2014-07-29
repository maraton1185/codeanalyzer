package ebook.module.conf.tree;

import ebook.module.tree.TreeItemInfo;

public class ContextInfo extends TreeItemInfo {

	public ContextInfo(ContextInfoOptions options) {
		super(options);
	}

	public ContextInfo() {
		super(null);
	}

	@Override
	public ContextInfoOptions getOptions() {
		return (ContextInfoOptions) super.getOptions();
	}

}
