package ebook.module.tree;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.List;

import org.eclipse.core.runtime.IPath;

public interface ITreeService {

	int rootId = 1;

	boolean check();

	List<ITreeItemInfo> getRoot();

	List<ITreeItemInfo> getChildren(int parent);

	boolean hasChildren(int parent);

	ITreeItemInfo get(int item);

	String getText(int id);

	void saveText(int id, String text);

	Connection getConnection() throws IllegalAccessException;

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

	String download(IPath zipFolder, ITreeItemSelection selection,
			String zipName, boolean clear) throws InvocationTargetException;

	ITreeItemInfo upload(String path, ITreeItemInfo item, boolean clear,
			boolean relative) throws InvocationTargetException;
}
