package codeanalyzer.module.db;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import codeanalyzer.core.models.BaseDbConnection;
import codeanalyzer.utils.Const;

public class DbConnection extends BaseDbConnection {

	public DbConnection() {
		super(new DbStructure());
	}

	@Override
	protected IPath getConnectionPath() {
		return new Path(Const.SYSTEM_DB_NAME);
	}

}
