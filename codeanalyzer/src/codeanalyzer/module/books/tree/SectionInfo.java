package codeanalyzer.module.books.tree;

import codeanalyzer.core.models.DbOptions;
import codeanalyzer.module.tree.TreeItemInfo;

public class SectionInfo extends TreeItemInfo {

	public SectionInfoOptions options;

	@Override
	public DbOptions getOptions() {
		return options;
	}
}
