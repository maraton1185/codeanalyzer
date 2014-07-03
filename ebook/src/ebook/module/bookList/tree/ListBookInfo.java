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

	private String path;

	public void setPath(String path) {
		this.path = path == null ? "" : path;
	}

	@Override
	public String getSuffix() {
		if (isGroup() && role != null)
			return role.getTitle();
		else
			return path;// ((ListBookInfoOptions) getOptions()).path;
	}

	public IPath getPath() {

		// return new Path(path);
		if (path == null)
			return null;
		//
		// ListBookInfoOptions options = (ListBookInfoOptions) getOptions();
		//

		return path.isEmpty() ? null : new Path(path);
	}
}
