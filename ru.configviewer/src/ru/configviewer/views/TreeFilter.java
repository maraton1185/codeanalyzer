package ru.configviewer.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import ru.configviewer.core.LineInfo;

public class TreeFilter extends ViewerFilter {

	private String text;

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(element instanceof LineInfo)
		{
			LineInfo line = (LineInfo)element;
			return line.title.toUpperCase().indexOf(text.toUpperCase())>=0;
		}else 
		return false;
	}

	public void setText(String text) {
		this.text = text;
		
	}

}
