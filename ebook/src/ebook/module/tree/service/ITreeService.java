package ebook.module.tree.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.List;

import ebook.module.tree.item.ITreeItemInfo;
import ebook.module.tree.item.ITreeItemSelection;

public interface ITreeService extends ITreeService2 {

	int rootId = 1;

	boolean check();

	String getText(int id);

	void saveText(int id, String text);

	Connection getConnection() throws IllegalAccessException;

	// ITreeItemInfo getParent(int item);

	ITreeItemInfo getLast(int parent);

	void add(ITreeItemInfo item, ITreeItemInfo parent, boolean sub)
			throws InvocationTargetException;

	void delete(ITreeItemInfo item);

	void delete(ITreeItemSelection sel);

	void updateOrder(List<ITreeItemInfo> items);

	ITreeItemInfo findInParent(String title, Integer parent);

	ITreeItemInfo getUploadRoot();

	void edit(ITreeItemInfo item);

	void setRoot(ITreeItemInfo item) throws InvocationTargetException;

	void dropRoot() throws InvocationTargetException;

}
