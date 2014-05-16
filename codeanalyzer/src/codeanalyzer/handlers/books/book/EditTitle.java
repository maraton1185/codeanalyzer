package codeanalyzer.handlers.books.book;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import codeanalyzer.core.AppManager;
import codeanalyzer.core.model.BookInfo;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_BOOK_LIST_DATA;

public class EditTitle {
	@Execute
	public void execute(BookInfo book) {

		AppManager.br.post(Const.EVENT_EDIT_TITLE_BOOK_LIST,
				new EVENT_UPDATE_BOOK_LIST_DATA(null, book));

	}

	@CanExecute
	public boolean canExecute(@Optional @Active BookInfo book) {
		return book != null;
	}
}