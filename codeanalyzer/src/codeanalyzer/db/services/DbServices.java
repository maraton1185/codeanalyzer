package codeanalyzer.db.services;

import codeanalyzer.core.interfaces.IDbServices;

public class DbServices implements IDbServices {

	DbGetService dbGetService;

	DbLoadService dbLoadService;

	/* (non-Javadoc)
	 * @see codeanalyzer.db.services.IDbServices#dbGetService()
	 */
	@Override
	public DbGetService get() {

		dbGetService = dbGetService == null ? new DbGetService() : dbGetService;
		return dbGetService;
	}

	/* (non-Javadoc)
	 * @see codeanalyzer.db.services.IDbServices#dbLoadService()
	 */
	@Override
	public DbLoadService load() {

		dbLoadService = dbLoadService == null ? new DbLoadService()
				: dbLoadService;
		return dbLoadService;
	}
}
