package ebook.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class SaveAll {
	@Execute
	public void execute(EPartService partService) {
		partService.saveAll(false);
	}

	// @CanExecute
	// public boolean canExecute(@Optional CurrentBookInfo book) {
	// return book != null;
	// }

}