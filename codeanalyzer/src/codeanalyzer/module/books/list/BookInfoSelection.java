package codeanalyzer.module.books.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import codeanalyzer.module.tree.ITreeItemInfo;
import codeanalyzer.module.tree.ITreeItemSelection;

public class BookInfoSelection implements ITreeItemSelection {

	private List<ITreeItemInfo> list = new ArrayList<ITreeItemInfo>();

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
}
