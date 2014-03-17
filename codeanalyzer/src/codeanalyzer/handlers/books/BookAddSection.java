package codeanalyzer.handlers.books;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.core.interfaces.IBookManager;

public class BookAddSection {
	@Execute
	public void execute(
			Shell shell,
			BookInfo book,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) BookSection section,
			IBookManager bm) {
		// NEXT add section
		// MessageDialog.openInformation(shell, "",
		// "add section to book " + book.getName());

		bm.addBookSection(book, section);

	}

	@CanExecute
	public boolean canExecute(@Optional @Active BookInfo book) {
		// BookInfo book = (BookInfo) w.getTransientData().get(
		// Const.WINDOW_CONTEXT);
		return book != null;
	}

}