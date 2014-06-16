package ebook.module.userList.tree;

import ebook.module.tree.TreeItemInfo;

public class UserInfo extends TreeItemInfo {

	public UserInfo(UserInfoOptions options) {
		super(options);
	}

	public UserInfo() {
		super(null);
	}

	@Override
	public String getSuffix() {

		UserInfoOptions options = getOptions();

		if (options.description == null)
			return "";

		return options.description.isEmpty() ? "" : options.description
				.substring(0, Math.min(options.description.length(), 20))
				.concat("...");
	}

	@Override
	public UserInfoOptions getOptions() {
		return (UserInfoOptions) super.getOptions();
	}

}
