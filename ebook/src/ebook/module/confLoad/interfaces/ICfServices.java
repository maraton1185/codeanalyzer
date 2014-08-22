package ebook.module.confLoad.interfaces;

import java.sql.Connection;

import ebook.module.confLoad.services.CfBuildService;
import ebook.module.confLoad.services.CfGetService;
import ebook.module.confLoad.services.CfLoadService;
import ebook.module.confLoad.services.TextParser;

public interface ICfServices {

	CfBuildService build(Connection connection);

	CfGetService get();

	CfLoadService load();

	TextParser parse();

}