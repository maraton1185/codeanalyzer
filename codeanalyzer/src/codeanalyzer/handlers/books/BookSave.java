package codeanalyzer.handlers.books;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import codeanalyzer.book.BookInfo;

public class BookSave {
	@Execute
	public void execute(EPartService partService) {
		partService.saveAll(false);
	}

	@CanExecute
	public boolean canExecute(@Optional BookInfo book) {
		return book != null;
	}

}