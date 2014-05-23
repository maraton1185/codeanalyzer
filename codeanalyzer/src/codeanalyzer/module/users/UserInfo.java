package codeanalyzer.module.users;

import codeanalyzer.core.models.DbOptions;
import codeanalyzer.module.tree.TreeItemInfo;

public class UserInfo extends TreeItemInfo {

	public UserInfoOptions options;

	@Override
	public String getSuffix() {
		return options.description == null ? "" : options.description
				.substring(0, 10).concat("...");
	}

	@Override
	public DbOptions getOptions() {
		return options;
	}
}
