package codeanalyzer.module.books.handlers.section;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import codeanalyzer.module.books.list.CurrentBookInfo;
import codeanalyzer.module.books.section.SectionInfo;

public class AddSub {
	@Execute
	public void execute(CurrentBookInfo book, @Active SectionInfo section) {

		book.sections().add_sub(section);

	}

	@CanExecute
	public boolean canExecute(@Optional @Active SectionInfo section) {
		return section != null;
	}

}