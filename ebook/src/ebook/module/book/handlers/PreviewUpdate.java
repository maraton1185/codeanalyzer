package ebook.module.book.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import ebook.module.book.tree.SectionInfo;
import ebook.module.book.views.PreviewView;

public class PreviewUpdate {

	@Execute
	public void execute(@Active MPart part, @Active SectionInfo section) {
		PreviewView view = (PreviewView) part.getObject();
		if (view != null)
			view.update(section);
	}

	@CanExecute
	public boolean canExecute(@Optional @Active SectionInfo section) {
		return section != null;
	}
}
