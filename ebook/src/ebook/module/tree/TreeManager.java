package ebook.module.tree;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

public abstract class TreeManager implements ITreeManager {

	public abstract TreeService srv();

	@Override
	public void add(ITreeItemInfo data, ITreeItemInfo parent, boolean sub)
			throws InvocationTargetException {

		srv().add(data, parent, sub);

	}

	@Override
	public void delete(ITreeItemSelection selection)
			throws InvocationTargetException {

		int parent = selection.getParent();

		Iterator<ITreeItemInfo> iterator = selection.iterator();
		while (iterator.hasNext())
			srv().delete(iterator.next());

		if (parent != 0)
			srv().selectLast(parent);

	}

	@Override
	public boolean save(ITreeItemInfo data) throws InvocationTargetException {
		srv().saveOptions(data);

		return true;
	}
}
