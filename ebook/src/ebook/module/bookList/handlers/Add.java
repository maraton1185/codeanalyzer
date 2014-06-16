package ebook.module.bookList.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.bookList.tree.ListBookInfo;
import ebook.module.confList.tree.ListConfInfo;

public class Add {
	@Execute
	public void execute(@Optional ListConfInfo db, Shell shell,
			@Optional @Named("bookListSelection") ListBookInfo book) {

		App.mng.blm().add(db, book, shell);

	}

}