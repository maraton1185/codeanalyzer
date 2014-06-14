package ebook.module.bookList.tree;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import ebook.module.tree.TreeItemInfo;
import ebook.module.userList.tree.UserInfo;

public class ListBookInfo extends TreeItemInfo {

	public ListBookInfo(ListBookInfoOptions options) {
		super(options);
	}

	public ListBookInfo() {
		super(null);
	}

	// public ListBookInfoOptions options;

	public UserInfo role;

	@Override
	public String getSuffix() {
		if (isGroup() && role != null)
			return role.getTitle();
		else
			return ((ListBookInfoOptions) getOptions()).path;
	}

	public IPath getPath() {

		if (getOptions() == null)
			return null;

		ListBookInfoOptions options = (ListBookInfoOptions) getOptions();

		return options.path.isEmpty() ? null : new Path(options.path);
	}

}
