package codeanalyzer.handlers.books.section;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import codeanalyzer.books.book.BookInfo;
import codeanalyzer.books.section.BookSection;
import codeanalyzer.core.AppManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_VIEW_DATA;

public class EditTitle {
	@Execute
	public void execute(BookInfo book, @Active BookSection section) {

		AppManager.br.post(Const.EVENT_EDIT_TITLE_CONTENT_VIEW,
				new EVENT_UPDATE_VIEW_DATA(book, section, null));

	}

	@CanExecute
	public boolean canExecute(@Optional @Active BookSection section) {
		return section != null;
	}

}