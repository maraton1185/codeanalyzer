package ebook.module.confList.tree;

import ebook.core.models.DbOptions;
import ebook.module.tree.TreeItemInfo;

public class ListConfInfo extends TreeItemInfo {

	public ListConfInfoOptions options;

	@Override
	public DbOptions getOptions() {
		return options;
	}
}
