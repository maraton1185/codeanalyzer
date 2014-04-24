package codeanalyzer.handlers.books;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;

public class SectionAddBlock {
	@Execute
	public void execute(BookInfo book, @Active BookSection section) {

		book.sections().add_sub(section, true);
	}

	@CanExecute
	public boolean canExecute(@Optional @Active BookSection section) {
		return section != null;
	}

}