package ebook.module.bookList.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.bookList.tree.ListBookInfo;

public class AddExisted {
	@Execute
	public void execute(final Shell shell, @Optional final ListBookInfo parent) {

		App.mng.blm().addToList(parent, shell);

	}
}