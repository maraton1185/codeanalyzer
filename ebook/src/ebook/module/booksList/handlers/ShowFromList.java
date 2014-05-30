package ebook.module.booksList.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.module.booksList.IBookListManager;
import ebook.module.booksList.tree.ListBookInfo;

public class ShowFromList {
	@Execute
	public void execute(ListBookInfo book, IBookListManager blm, Shell shell) {
		blm.openBook(book.getPath(), shell);
	}

	@CanExecute
	public boolean canExecute(@Optional ListBookInfo book) {
		return book != null && !book.isGroup();
	}

}