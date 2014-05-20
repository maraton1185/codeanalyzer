package codeanalyzer.module.books.list;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import codeanalyzer.module.tree.TreeItemInfo;

public class BookInfo extends TreeItemInfo {

	public BookInfoOptions options;

	@Override
	public String getSuffix() {
		return options.path;
	}

	public IPath getPath() {

		return options.path.isEmpty() ? null : new Path(options.path);
	}
}
