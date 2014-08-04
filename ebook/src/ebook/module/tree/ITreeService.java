package ebook.module.tree;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface ITreeService {

	int rootId = 1;

	List<ITreeItemInfo> getRoot();

	List<ITreeItemInfo> getChildren(int parent);

	boolean hasChildren(int parent);

	ITreeItemInfo get(int item);

	// ITreeItemInfo getParent(int item);

	ITreeItemInfo getLast(int parent);

	void add(ITreeItemInfo item, ITreeItemInfo parent, boolean sub)
			throws InvocationTargetException;

	void delete(ITreeItemInfo item);

	void delete(ITreeItemSelection sel);

	Boolean setParent(ITreeItemInfo item, ITreeItemInfo target);

	Boolean setAfter(ITreeItemInfo item, ITreeItemInfo target);

	Boolean setBefore(ITreeItemInfo item, ITreeItemInfo target);

	void updateOrder(List<ITreeItemInfo> items);

	void saveTitle(ITreeItemInfo object);

	ITreeItemInfo getSelected();
}
