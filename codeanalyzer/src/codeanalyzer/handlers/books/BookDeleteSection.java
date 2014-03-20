package codeanalyzer.handlers.books;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;

public class BookDeleteSection {
	@Execute
	public void execute(Shell shell, BookInfo book, @Active BookSection section) {
		// if (MessageDialog.openConfirm(shell, Strings.get("appTitle"),
		// "Удалить раздел?"))
		book.sections().delete(section);
	}

	@CanExecute
	public boolean canExecute(@Optional @Active BookSection section) {
		return section != null;
	}

}