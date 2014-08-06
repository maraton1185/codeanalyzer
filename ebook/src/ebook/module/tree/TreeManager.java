package ebook.module.tree;

import java.util.Iterator;

import org.eclipse.swt.widgets.Shell;

public abstract class TreeManager implements ITreeManager {

	public TreeManager(TreeService srv) {
		super();
		this.srv = srv;
	}

	protected TreeService srv;

	@Override
	public void delete(ITreeItemSelection selection, Shell shell) {

		// srv.delete(selection);

		int parent = selection.getParent();

		Iterator<ITreeItemInfo> iterator = selection.iterator();
		while (iterator.hasNext())
			srv.delete(iterator.next());

		if (parent != 0)
			srv.selectLast(parent);

	}

	@Override
	public void add(ITreeItemInfo parent, Shell shell) {

	}

	@Override
	public void addToList(ITreeItemInfo parent, Shell shell) {

	}

	@Override
	public void addGroup(ITreeItemInfo parent, Shell shell) {

	}

	@Override
	public void addSubGroup(ITreeItemInfo parent, Shell shell) {

	}

	@Override
	public boolean save(ITreeItemInfo data, Shell shell) {
		return false;
	}

}
