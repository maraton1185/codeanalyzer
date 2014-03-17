package codeanalyzer.handlers.books;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.book.BookSection;

public class BookDeleteSection {
	@Execute
	public void execute(Shell shell) {
		MessageDialog.openInformation(shell, "", "delete section");
		// NEXT delete section
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) BookSection section) {
		return section != null;
	}

}