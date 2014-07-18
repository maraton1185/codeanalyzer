package ebook.module.book;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IPath;

import ebook.core.App;
import ebook.core.models.BaseDbPathConnection;
import ebook.module.tree.ITreeItemInfo;

public class BookConnection extends BaseDbPathConnection {

	ITreeItemInfo treeItem;

	public BookConnection(IPath path, boolean check)
			throws InvocationTargetException {

		super(path, new BookStructure(), check);

	}

	public BookConnection(String name) throws InvocationTargetException {

		super(name, new BookStructure());

	}

	BookService service;

	public BookService srv() {

		service = service == null ? App.srv.bs(this) : service;

		return service;
	}

	public ITreeItemInfo getTreeItem() {

		if (treeItem == null)
			return App.srv.bls().getBookTreeItem(getConnectionPath());
		else
			return treeItem;
	}

	@Override
	public String getWindowTitle() {
		return getTreeItem().getTitle();
	}

}
