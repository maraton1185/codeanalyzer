package ebook.module.userList.tree;

import org.eclipse.swt.graphics.Image;

import ebook.module.tree.item.TreeItemInfo;
import ebook.module.tree.service.ITreeService;
import ebook.utils.Utils;

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

	@Override
	public Image getListImage() {
		if (getParent() == ITreeService.rootId && isGroup())
			return Utils.getImage("lock.png");
		else if (isRoot() && getId() != ITreeService.rootId)
			return Utils.getImage("filter.png");
		else
			return null;
	}

}
