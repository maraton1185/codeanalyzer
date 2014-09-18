package ebook.module.bookList.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.bookList.tree.ListBookInfo;

public class OpenBookInBrowser {
	@Execute
	public void execute(ListBookInfo book, Shell shell) {

		Program.launch(App.getJetty().host()
				+ App.getJetty().book(book.getId()));
	}

	@CanExecute
	public boolean canExecute(@Optional ListBookInfo book) {
		return book != null && !book.isGroup() && App.getJetty().isStarted();
	}
}
