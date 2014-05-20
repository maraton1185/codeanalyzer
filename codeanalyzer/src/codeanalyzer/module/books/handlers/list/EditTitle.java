package codeanalyzer.module.books.handlers.list;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import codeanalyzer.core.AppManager;
import codeanalyzer.core.Events;
import codeanalyzer.core.Events.EVENT_UPDATE_TREE_DATA;
import codeanalyzer.module.books.list.BookInfo;

public class EditTitle {
	@Execute
	public void execute(BookInfo book) {

		AppManager.br.post(Events.EVENT_EDIT_TITLE_BOOK_LIST,
				new EVENT_UPDATE_TREE_DATA(null, book));

	}

	@CanExecute
	public boolean canExecute(@Optional @Active BookInfo book) {
		return book != null;
	}
}