package codeanalyzer.module.users;

import java.util.Iterator;
import java.util.List;

import codeanalyzer.module.tree.ITreeItemInfo;
import codeanalyzer.module.tree.ITreeItemSelection;

public class UserInfoSelection implements ITreeItemSelection {

	private List<ITreeItemInfo> list;

	@Override
	public int getParent() {
		if (list.isEmpty())
			return 0;

		return list.get(0).getParent();
	}

	@Override
	public void add(ITreeItemInfo item) {
		list.add(item);

	}

	@Override
	public Iterator<ITreeItemInfo> iterator() {
		return list.iterator();
	}

}
