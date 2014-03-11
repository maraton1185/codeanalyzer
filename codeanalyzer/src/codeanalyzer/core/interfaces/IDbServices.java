package codeanalyzer.core.interfaces;

import codeanalyzer.db.services.DbGetService;
import codeanalyzer.db.services.DbLoadService;

public interface IDbServices {

	DbGetService get();

	DbLoadService load();

}