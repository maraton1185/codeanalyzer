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

	private int section = 0;

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;

	}

}
