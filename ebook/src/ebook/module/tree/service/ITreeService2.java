package ebook.module.tree.service;

import java.util.List;

import ebook.module.tree.item.ITreeItemInfo;

public interface ITreeService2 {
	List<ITreeItemInfo> getRoot();

	List<ITreeItemInfo> getChildren(int parent);

	boolean hasChildren(int parent);

	ITreeItemInfo get(int item);

	void saveTitle(ITreeItemInfo object);

	Boolean setAfter(ITreeItemInfo item, ITreeItemInfo target);

	Boolean setBefore(ITreeItemInfo item, ITreeItemInfo target);

	Boolean setParent(ITreeItemInfo item, ITreeItemInfo target);

	ITreeItemInfo getSelected();
}
