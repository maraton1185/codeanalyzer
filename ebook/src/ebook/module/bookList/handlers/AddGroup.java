package ebook.module.bookList.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.bookList.tree.ListBookInfo;

public class AddGroup {
	@Execute
	public void execute(Shell shell, @Optional ListBookInfo book) {

		App.mng.blm().addGroup(book, shell);

	}

}