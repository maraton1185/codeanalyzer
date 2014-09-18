package ebook.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import ebook.module.book.views.SectionView;

public class Save {
	@Execute
	public void execute(EPartService partService, @Active MPart part) {

		if (part.getObject() instanceof SectionView)
			((SectionView) part.getObject()).save();
		else {
			part.setDirty(true);
			partService.savePart(part, false);
		}

	}

	@CanExecute
	public boolean canExecute(@Optional @Active MPart part) {
		return part != null;
	}

}