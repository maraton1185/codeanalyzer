package ebook.module.books.tree;

import ebook.core.models.DbOptions;
import ebook.module.tree.TreeItemInfo;

public class SectionInfo extends TreeItemInfo {

	public SectionInfoOptions options;

	@Override
	public DbOptions getOptions() {
		return options;
	}

	@Override
	public boolean isTitleIncrement() {
		return true;
	}
}
