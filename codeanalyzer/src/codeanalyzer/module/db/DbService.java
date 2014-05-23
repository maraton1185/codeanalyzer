package codeanalyzer.module.db;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import codeanalyzer.core.models.BaseDbService;
import codeanalyzer.utils.Const;

public class DbService extends BaseDbService {

	public DbService() {
		super(new DbStructure());
	}

	@Override
	protected IPath getConnectionPath() {
		return new Path(Const.SYSTEM_DB_NAME);
	}

}
