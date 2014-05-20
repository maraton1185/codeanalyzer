package codeanalyzer.module.books.handlers.section;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import codeanalyzer.core.Events;
import codeanalyzer.module.books.list.CurrentBookInfo;
import codeanalyzer.module.books.section.SectionInfo;

public class AddBlock {
	@Execute
	public void execute(
			CurrentBookInfo book,
			@Active MPart part,
			@Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {

		// Object o = part.getObject();
		// if (o instanceof SectionView)
		// book.sections().add_sub(((SectionView) o).getSection(), true);

		book.sections().add_sub(section, true);
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {
		return section != null;
	}

}