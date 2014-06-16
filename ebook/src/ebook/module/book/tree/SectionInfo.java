package ebook.module.book.tree;

import ebook.module.tree.TreeItemInfo;

public class SectionInfo extends TreeItemInfo {

	public SectionInfo(SectionInfoOptions options) {
		super(options);
	}

	public SectionInfo() {
		super(null);
	}

	@Override
	public boolean isTitleIncrement() {
		return true;
	}

	@Override
	public SectionInfoOptions getOptions() {
		return (SectionInfoOptions) super.getOptions();
	}

}
