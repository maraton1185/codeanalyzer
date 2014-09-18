package ebook.module.book.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import ebook.module.book.views.SectionView;

public class Update {

	@Execute
	public void execute(@Active MPart part) {
		SectionView view = (SectionView) part.getObject();
		if (view != null)
			view.update();
	}

	// @CanExecute
	// public boolean canExecute(
	// @Optional @Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo
	// section) {
	// return section != null;
	// }

}