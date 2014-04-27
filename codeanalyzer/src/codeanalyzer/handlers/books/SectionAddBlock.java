package codeanalyzer.handlers.books;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.views.books.SectionView;

public class SectionAddBlock {
	@Execute
	public void execute(BookInfo book, @Active MPart part) {

		Object o = part.getObject();
		if (o instanceof SectionView)
			book.sections().add_sub(((SectionView) o).getSection(), true);
	}

	@CanExecute
	public boolean canExecute(@Optional @Active BookSection section) {
		return section != null;
	}

}