package codeanalyzer.module.users.tree;

import codeanalyzer.core.models.DbOptions;
import codeanalyzer.module.tree.TreeItemInfo;

public class UserInfo extends TreeItemInfo {

	public UserInfoOptions options;

	@Override
	public String getSuffix() {

		if (options.description == null)
			return "";

		return options.description.isEmpty() ? ""
				: options.description
				.substring(0, Math.min(options.description.length(), 20))
				.concat(
						"...");
	}

	@Override
	public DbOptions getOptions() {
		return options;
	}
}
