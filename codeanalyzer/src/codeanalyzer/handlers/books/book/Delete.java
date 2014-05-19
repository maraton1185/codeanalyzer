package codeanalyzer.handlers.books.book;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.books.interfaces.IBookManager;
import codeanalyzer.cf.interfaces.ICf;
import codeanalyzer.core.model.BookInfo;

public class Delete {
	@Execute
	public void execute(@Optional ICf db, Shell shell, IBookManager bm,
			BookInfo book) {

		bm.delete(book);
	}

	@CanExecute
	public boolean canExecute(@Optional @Active BookInfo book) {
		return book != null;
	}
}