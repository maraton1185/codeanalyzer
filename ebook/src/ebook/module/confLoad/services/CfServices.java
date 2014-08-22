package ebook.module.confLoad.services;

import java.sql.Connection;

import ebook.module.confLoad.interfaces.ICfServices;

public class CfServices implements ICfServices {

	CfGetService dbGetService;

	@Override
	public CfGetService get() {

		dbGetService = dbGetService == null ? new CfGetService() : dbGetService;
		return dbGetService;
	}

	CfLoadService dbLoadService;

	@Override
	public CfLoadService load() {

		dbLoadService = dbLoadService == null ? new CfLoadService()
				: dbLoadService;
		return dbLoadService;
	}

	private TextParser parse;

	@Override
	public TextParser parse() {
		parse = parse == null ? new TextParser() : parse;
		return parse;
	}

	CfBuildService build;

	@Override
	public CfBuildService build(Connection con) {
		build = build == null ? new CfBuildService() : build;
		build.setConnection(con);
		return build;
	}

}
