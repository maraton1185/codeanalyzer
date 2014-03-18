package codeanalyzer.handlers.books;

import javax.inject.Named;

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

		bm.sections().add(section);

	}

	// @CanExecute
	// public boolean canExecute(@Optional @Active BookInfo book) {
	// // BookInfo book = (BookInfo) w.getTransientData().get(
	// // Const.WINDOW_CONTEXT);
	// return book != null;
	// }

	@CanExecute
	public boolean canExecute(
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) BookSection section) {
		return section != null;
	}

}