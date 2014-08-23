package ebook.module.text.strategy;

import org.eclipse.jface.text.DefaultTextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;

public class TextDoubleClickStrategy extends DefaultTextDoubleClickStrategy {

	@Override
	public void doubleClicked(ITextViewer text) {

		super.doubleClicked(text);

		// if(text instanceof TextSourceViewer)
		// ((TextSourceViewer)text).doubleClicked();

	}

}
