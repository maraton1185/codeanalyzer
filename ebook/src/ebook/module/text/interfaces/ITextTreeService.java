package ebook.module.text.interfaces;

import java.sql.Connection;
import java.util.List;

import ebook.module.conf.tree.ContextInfo;
import ebook.module.text.model.GotoDefinitionData;
import ebook.module.tree.item.ITreeItemInfo;

public interface ITextTreeService {

	void saveText(int id, String text);

	List<ITreeItemInfo> getChildren(int id);

	String getText(int id);

	ITreeItemInfo findInParent(String title, Integer id);

	ITreeItemInfo get(int id);

	Connection getConnection() throws IllegalAccessException;

	ITreeItemInfo getModule(ITreeItemInfo item);

	List<ITreeItemInfo> getParents(ITreeItemInfo item);

	String getPath(ContextInfo item);

	String getPath(ContextInfo item, List<String> path);

	ContextInfo getByPath(String path);

	List<ITreeItemInfo> getDefinitions(GotoDefinitionData data);
}
