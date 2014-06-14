package ebook.module.bookList.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.module.bookList.IBookListManager;
import ebook.module.bookList.tree.ListBookInfoSelection;
import ebook.module.cf.interfaces.ICf;

public class Delete {
	@Execute
	public void execute(@Optional ICf db, Shell shell, IBookListManager bm,
			ListBookInfoSelection selection) {

		bm.delete(selection);
	}

	@CanExecute
	public boolean canExecute(@Optional @Active ListBookInfoSelection selection) {
		return selection != null;
	}
}