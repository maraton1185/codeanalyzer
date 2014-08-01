package ebook.module.book;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import ebook.core.App;
import ebook.core.models.BaseDbPathConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.module.tree.ITreeItemInfo;
import ebook.utils.PreferenceSupplier;

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

		service = service == null ? App.srv.bk(this) : service;

		return service;
	}

	SectionContextService ctxsrv;

	public SectionContextService ctxsrv(SectionInfo section) {

		ctxsrv = ctxsrv == null ? new SectionContextService(this, section)
				: ctxsrv;

		return ctxsrv;
	}

	public ITreeItemInfo getTreeItem() {

		if (treeItem == null)
			return App.srv.bl().getBookTreeItem(getName());
		else
			return treeItem;
	}

	@Override
	public String getWindowTitle() {
		return getTreeItem().getTitle();
	}

	@Override
	protected IPath getBasePath() {

		return new Path(
				PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY));

	}
}
