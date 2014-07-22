package ebook.module.bookList.tree;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Image;

import ebook.module.tree.TreeItemInfo;

public class ListBookInfo extends TreeItemInfo {

	public ListBookInfo(ListBookInfoOptions options) {
		super(options);
	}

	public ListBookInfo() {
		super(null);
	}

	private Image image;

	public Image getImage() {
		return image;
	}

	public boolean hasImage() {
		return image != null;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	// public UserInfo role;

	private String path;

	public void setPath(String path) {
		this.path = path == null ? "" : path;
	}

	@Override
	public String getSuffix() {
		if (isGroup())
			// return role.getTitle();
			return "";
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
