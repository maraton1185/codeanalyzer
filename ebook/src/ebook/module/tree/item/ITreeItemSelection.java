package ebook.module.tree.item;

import java.util.Iterator;

public interface ITreeItemSelection {

	int getParent();

	void add(ITreeItemInfo item);

	Iterator<ITreeItemInfo> iterator();

	boolean isEmpty();

	String getTitle();

}
