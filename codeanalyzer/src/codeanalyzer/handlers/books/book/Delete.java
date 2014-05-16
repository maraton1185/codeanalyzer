package codeanalyzer.handlers.books.book;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.cf.interfaces.ICf;
import codeanalyzer.core.components.ITreeService;
import codeanalyzer.core.model.BookInfo;
import codeanalyzer.db.interfaces.IDbService;

public class Delete {
	@Execute
	public void execute(@Optional ICf db, Shell shell, IDbService bm,
			BookInfo book) {

		((ITreeService) bm).delete(book);
	}

	@CanExecute
	public boolean canExecute(@Optional @Active BookInfo book) {
		return book != null;
	}
}