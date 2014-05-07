package codeanalyzer.handlers.books.section;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import codeanalyzer.books.book.BookInfo;
import codeanalyzer.books.section.SectionInfo;

public class AddSub {
	@Execute
	public void execute(BookInfo book, @Active SectionInfo section) {

		book.sections().add_sub(section);

	}

	@CanExecute
	public boolean canExecute(@Optional @Active SectionInfo section) {
		return section != null;
	}

}