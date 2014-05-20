package codeanalyzer.module.cf.services;

import codeanalyzer.module.cf.interfaces.ICfServices;

public class CfServices implements ICfServices {

	CfGetService dbGetService;

	CfLoadService dbLoadService;

	/* (non-Javadoc)
	 * @see codeanalyzer.db.services.IDbServices#dbGetService()
	 */
	@Override
	public CfGetService get() {

		dbGetService = dbGetService == null ? new CfGetService() : dbGetService;
		return dbGetService;
	}

	/* (non-Javadoc)
	 * @see codeanalyzer.db.services.IDbServices#dbLoadService()
	 */
	@Override
	public CfLoadService load() {

		dbLoadService = dbLoadService == null ? new CfLoadService()
				: dbLoadService;
		return dbLoadService;
	}
}
