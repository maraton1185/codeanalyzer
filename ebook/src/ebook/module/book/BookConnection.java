package ebook.module.book;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import ebook.core.App;
import ebook.module.book.tree.SectionInfo;
import ebook.module.db.BaseDbPathConnection;
import ebook.module.tree.ITreeItemInfo;
import ebook.utils.PreferenceSupplier;

public class BookConnection extends BaseDbPathConnection {

	ITreeItemInfo treeItem;

	public BookConnection(IPath path, boolean check)
			throws InvocationTargetException {

		super(path, new BookStructure(), check, false);

	}

	public BookConnection(String name) throws InvocationTargetException {

		super(name, new BookStructure());

	}

	BookService service;

	public BookService srv() {

		service = service == null ? App.srv.bk(this) : service;

		return service;
	}

	ContextService ctxsrv;

	public ContextService ctxsrv(SectionInfo section) {

		if (ctxsrv != null) {
			ctxsrv = ctxsrv.getSection().equals(section) ? ctxsrv
					: new ContextService(this, section);
		} else
			ctxsrv = new ContextService(this, section);
		// ctxsrv = ctxsrv == null ? new ContextService(this, section) : ctxsrv;

		return ctxsrv;
	}

	@Override
	public ITreeItemInfo getTreeItem() {

		if (treeItem == null)
			treeItem = App.srv.bl().getTreeItem(getName(), getFullName());

		return treeItem;
	}

	@Override
	protected IPath getBasePath() {

		return new Path(
				PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY));

	}

}
