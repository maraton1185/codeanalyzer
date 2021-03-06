package codeanalyzer.module.tree;

import java.util.Iterator;

public interface ITreeItemSelection {

	int getParent();

	void add(ITreeItemInfo item);

	Iterator<ITreeItemInfo> iterator();

}
