package codeanalyzer.handlers.books;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.book.BookInfo;

public class BookAddSection {
	@Execute
	public void execute(Shell shell, BookInfo book) {
		// NEXT add section
		MessageDialog.openInformation(shell, "",
				"add section to book " + book.getName());
	}

	@CanExecute
	public boolean canExecute(@Optional @Active BookInfo book) {
		// BookInfo book = (BookInfo) w.getTransientData().get(
		// Const.WINDOW_CONTEXT);
		return book != null;
	}

}