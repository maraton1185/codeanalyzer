package ebook.module.text.strategy;

import org.eclipse.jface.text.DefaultTextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;

import ebook.core.App;
import ebook.utils.Events;

public class TextDoubleClickStrategy extends DefaultTextDoubleClickStrategy {

	@Override
	public void doubleClicked(ITextViewer text) {

		super.doubleClicked(text);

		App.br.post(Events.EVENT_TEXT_VIEW_DOUBLE_CLICK, text);
		// if(text instanceof TextSourceViewer)
		// ((TextSourceViewer)text).doubleClicked();

	}

}
