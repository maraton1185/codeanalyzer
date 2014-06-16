package ebook.module.tree;

import org.eclipse.swt.widgets.Shell;

public interface ITreeManager {

	void add(ITreeItemInfo parent, Shell shell);

	void addToList(ITreeItemInfo parent, Shell shell);

	void addGroup(ITreeItemInfo parent, Shell shell);

	void addSubGroup(ITreeItemInfo parent, Shell shell);

	void delete(ITreeItemSelection selection, Shell shell);

	boolean save(ITreeItemInfo data, Shell shell);

}
