package ebook.module.tree.manager;

import org.eclipse.swt.widgets.Shell;

import ebook.module.tree.item.ITreeItemInfo;
import ebook.module.tree.item.ITreeItemSelection;

public interface ITreeManager {

	void add(ITreeItemInfo parent, Shell shell);

	void addToList(ITreeItemInfo parent, Shell shell);

	void addGroup(ITreeItemInfo parent, Shell shell);

	void addSubGroup(ITreeItemInfo parent, Shell shell);

	void delete(ITreeItemSelection selection, Shell shell);

	boolean save(ITreeItemInfo data, Shell shell);

}
