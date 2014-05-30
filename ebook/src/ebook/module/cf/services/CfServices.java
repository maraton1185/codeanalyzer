package ebook.module.cf.services;

import ebook.module.cf.interfaces.ICfServices;

public class CfServices implements ICfServices {

	CfGetService dbGetService;

	CfLoadService dbLoadService;

	/* (non-Javadoc)
	 * @see ebook.db.services.IDbServices#dbGetService()
	 */
	@Override
	public CfGetService get() {

		dbGetService = dbGetService == null ? new CfGetService() : dbGetService;
		return dbGetService;
	}

	/* (non-Javadoc)
	 * @see ebook.db.services.IDbServices#dbLoadService()
	 */
	@Override
	public CfLoadService load() {

		dbLoadService = dbLoadService == null ? new CfLoadService()
				: dbLoadService;
		return dbLoadService;
	}
}
