package codeanalyzer.module.books.list;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import codeanalyzer.core.models.DbOptions;
import codeanalyzer.module.tree.TreeItemInfo;
import codeanalyzer.module.users.UserInfo;

public class ListBookInfo extends TreeItemInfo {

	public ListBookInfoOptions options;

	public UserInfo role;

	@Override
	public String getSuffix() {
		if (isGroup() && role != null)
			return role.title;
		else
			return options.path;
	}

	public IPath getPath() {

		if (options == null)
			return null;

		return options.path.isEmpty() ? null : new Path(options.path);
	}

	@Override
	public DbOptions getOptions() {
		return options;
	}
}
