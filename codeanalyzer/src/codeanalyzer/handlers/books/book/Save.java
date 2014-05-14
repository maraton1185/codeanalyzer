package codeanalyzer.handlers.books.book;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import codeanalyzer.books.book.CurrentBookInfo;

public class Save {
	@Execute
	public void execute(EPartService partService) {
		partService.saveAll(false);
	}

	@CanExecute
	public boolean canExecute(@Optional CurrentBookInfo book) {
		return book != null;
	}

}