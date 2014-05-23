package codeanalyzer.module.books.handlers.list;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.module.books.interfaces.IBookListManager;
import codeanalyzer.module.books.list.ListBookInfoSelection;
import codeanalyzer.module.cf.interfaces.ICf;

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