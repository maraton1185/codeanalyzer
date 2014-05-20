package codeanalyzer.handlers.books.book;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.books.book.BookInfoSelection;
import codeanalyzer.books.interfaces.IBookManager;
import codeanalyzer.cf.interfaces.ICf;

public class Delete {
	@Execute
	public void execute(@Optional ICf db, Shell shell, IBookManager bm,
			BookInfoSelection selection) {

		bm.delete(selection);
	}

	@CanExecute
	public boolean canExecute(@Optional @Active BookInfoSelection selection) {
		return selection != null;
	}
}