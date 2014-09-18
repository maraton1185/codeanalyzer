package ebook.module.book.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import ebook.module.book.views.PreviewView;

public class BlockPreview {
	@Execute
	public void execute(@Active MPart part) {
		PreviewView view = (PreviewView) part.getObject();
		if (view != null)
			view.triggerBlock();
	}

}