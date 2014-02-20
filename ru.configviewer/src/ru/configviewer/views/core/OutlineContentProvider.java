package ru.configviewer.views.core;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import ru.configviewer.core.LineInfo;
import ru.configviewer.views.MainView;

public class OutlineContentProvider implements ITreeContentProvider  {

	private static final Object[] EMPTY_ARRAY = new Object[0];
//	private OutlineView view;
	 
	public OutlineContentProvider(MainView outlineView) {
//		view = outlineView;
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Collection)
			return ((Collection<LineInfo>) inputElement).toArray();
		else
			return EMPTY_ARRAY;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return false;
	}


}
