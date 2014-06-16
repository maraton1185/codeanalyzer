package ebook.module.bookList.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.bookList.tree.ListBookInfo;
import ebook.module.cf.interfaces.ICf;

public class AddSubGroup {
	@Execute
	public void execute(@Optional ICf db, Shell shell,
			@Optional ListBookInfo book) {

		App.mng.blm().addSubGroup(book, shell);

	}
}