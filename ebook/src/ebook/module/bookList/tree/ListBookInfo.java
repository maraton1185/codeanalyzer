package ebook.module.bookList.tree;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Image;

import ebook.core.App;
import ebook.module.tree.item.TreeItemInfo;
import ebook.module.tree.service.ITreeService;
import ebook.utils.Utils;

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
	private boolean aclEmplicit;

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

	@Override
	public void setACL() {
		aclEmplicit = App.srv.acl().hasExplicit(getId());
	}

	@Override
	public Image getListImage() {
		if (aclEmplicit)
			return Utils.getImage("lock.png");
		else if (isRoot() && getId() != ITreeService.rootId)
			return Utils.getImage("filter.png");
		else
			return null;
	}

}
