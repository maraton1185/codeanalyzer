package ebook.module.conf;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import ebook.core.App;
import ebook.core.models.BaseDbPathConnection;
import ebook.module.tree.ITreeItemInfo;
import ebook.utils.PreferenceSupplier;

public class ConfConnection extends BaseDbPathConnection {

	public ConfConnection(IPath path) throws InvocationTargetException {
		super(path, new ConfStructure(), true);
	}

	public ConfConnection(String name) throws InvocationTargetException {
		super(name, new ConfStructure());
	}

	@Override
	protected IPath getBasePath() {

		return new Path(
				PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_CONF_DIRECTORY));

	}

	private ConfService service;

	@Override
	public ConfService srv() {

		service = service == null ? App.srv.cf(this) : service;

		return service;
	}

	ITreeItemInfo treeItem;

	@Override
	public ITreeItemInfo getTreeItem() {
		if (treeItem == null)
			treeItem = App.srv.cl().getTreeItem(getName());

		return treeItem;
	}

	// @Override
	// public String getWindowTitle() {
	// return getName();
	// }

}
