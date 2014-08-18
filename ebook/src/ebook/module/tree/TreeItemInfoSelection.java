package ebook.module.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TreeItemInfoSelection implements ITreeItemSelection {

	protected List<ITreeItemInfo> list = new ArrayList<ITreeItemInfo>();

	@Override
	public Iterator<ITreeItemInfo> iterator() {
		return list.iterator();
	}

	@Override
	public void add(ITreeItemInfo item) {
		list.add(item);

	}

	@Override
	public int getParent() {
		if (list.isEmpty())
			return 0;

		return list.get(0).getParent();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public String getTitle() {

		if (isEmpty())
			return "";

		String result = "";
		Iterator<ITreeItemInfo> iterator = list.iterator();
		while (iterator.hasNext()) {
			ITreeItemInfo item = iterator.next();
			result = result.concat(item.getTitle() + ", ");
		}
		// TODO Auto-generated method stub
		return result.substring(0, result.length() - 2);
	}

}
