package ebook.module.bookList.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.bookList.tree.ListBookInfo;

public class ShowFromList {
	@Execute
	public void execute(ListBookInfo book, Shell shell) {
		App.mng.blm().openBook(book.getPath(), shell);
	}

	@CanExecute
	public boolean canExecute(@Optional ListBookInfo book) {
		return book != null && !book.isGroup();
	}

}