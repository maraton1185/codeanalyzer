package ebook.module.db;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import ebook.core.models.BaseDbConnection;
import ebook.utils.Const;

public class DbConnection extends BaseDbConnection {

	public DbConnection() {
		super(new DbStructure());
	}

	@Override
	protected IPath getConnectionPath() {
		return new Path(Const.SYSTEM_DB_NAME);
	}

}