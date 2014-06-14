package ebook.module.confList;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IPath;

import ebook.module.confList.tree.ListConfInfo;

public interface IConfManager {

	void Add(String value, ListConfInfo parent)
			throws InvocationTargetException;

	void addToList(IPath path, ListConfInfo parent)
			throws InvocationTargetException;

	void addGroup(ListConfInfo data, ListConfInfo selected, boolean sub)
			throws InvocationTargetException;

}
