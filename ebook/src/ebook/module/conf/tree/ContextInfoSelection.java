package ebook.module.conf.tree;

import ebook.module.tree.item.TreeItemInfoSelection;

public class ContextInfoSelection extends TreeItemInfoSelection {

	public int getList() {
		if (isEmpty())
			return 0;
		return ((ContextInfo) list.get(0)).getList();
	}

}
