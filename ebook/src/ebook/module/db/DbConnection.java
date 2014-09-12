package ebook.module.db;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import ebook.module.tree.item.ITreeItemInfo;
import ebook.utils.Const;

public class DbConnection extends BaseDbConnection {

	public DbConnection() {
		super(new DbStructure());
	}

	@Override
	protected IPath getConnectionPath() {
		return new Path(Const.SYSTEM_DB_NAME);
	}

	@Override
	public ITreeItemInfo getTreeItem() {
		return null;
	}

	@Override
	public String getFullName() {

		return getConnectionPath().toString() + Const.DEFAULT_DB_EXTENSION;

	}

}
