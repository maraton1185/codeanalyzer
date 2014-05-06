package codeanalyzer.handlers.books;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.utils.Const;

public class SectionAddBlock {
	@Execute
	public void execute(
			BookInfo book,
			@Active MPart part,
			@Active @Named(Const.CONTEXT_ACTIVE_VIEW_SECTION) BookSection section) {

		// Object o = part.getObject();
		// if (o instanceof SectionView)
		// book.sections().add_sub(((SectionView) o).getSection(), true);

		book.sections().add_sub(section, true);
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Active @Named(Const.CONTEXT_ACTIVE_VIEW_SECTION) BookSection section) {
		return section != null;
	}

}