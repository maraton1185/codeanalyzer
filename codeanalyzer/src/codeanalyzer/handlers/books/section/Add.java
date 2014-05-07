package codeanalyzer.handlers.books.section;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.books.book.BookInfo;
import codeanalyzer.books.section.BookSection;

public class Add {
	@Execute
	public void execute(Shell shell, BookInfo book, @Active BookSection section) {

		book.sections().add(section);

	}

	@CanExecute
	public boolean canExecute(@Optional @Active BookSection section) {
		return section != null;
	}

}