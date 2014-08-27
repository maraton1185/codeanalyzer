package ebook.module.text.interfaces;

import java.sql.Connection;
import java.util.List;

import ebook.module.tree.ITreeItemInfo;

public interface ITextTreeService {

	void saveText(int id, String text);

	List<ITreeItemInfo> getChildren(int id);

	String getText(int id);

	ITreeItemInfo findInParent(String title, Integer id);

	ITreeItemInfo get(int parent);

	Connection getConnection() throws IllegalAccessException;
}
