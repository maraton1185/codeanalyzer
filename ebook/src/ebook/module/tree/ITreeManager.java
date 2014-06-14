package ebook.module.tree;

import java.lang.reflect.InvocationTargetException;

public interface ITreeManager {

	void add(ITreeItemInfo data, ITreeItemInfo user, boolean sub)
			throws InvocationTargetException;

	void delete(ITreeItemSelection selection) throws InvocationTargetException;

	boolean save(ITreeItemInfo data) throws InvocationTargetException;

}
