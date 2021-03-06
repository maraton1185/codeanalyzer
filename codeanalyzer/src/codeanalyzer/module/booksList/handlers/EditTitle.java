package codeanalyzer.module.booksList.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import codeanalyzer.core.App;
import codeanalyzer.module.booksList.tree.ListBookInfo;
import codeanalyzer.utils.Events;
import codeanalyzer.utils.Events.EVENT_UPDATE_TREE_DATA;

public class EditTitle {
	@Execute
	public void execute(ListBookInfo book) {

		App.br.post(Events.EVENT_EDIT_TITLE_BOOK_LIST,
				new EVENT_UPDATE_TREE_DATA(null, book));

	}

	@CanExecute
	public boolean canExecute(@Optional @Active ListBookInfo book) {
		return book != null;
	}
}