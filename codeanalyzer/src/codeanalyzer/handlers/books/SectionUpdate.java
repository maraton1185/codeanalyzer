package codeanalyzer.handlers.books;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.core.AppManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_CONTENT_VIEW_DATA;

public class SectionUpdate {
	@Execute
	public void execute(
			BookInfo book,
			@Active @Named(Const.CONTEXT_ACTIVE_VIEW_SECTION) BookSection section) {
		AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW,
				new EVENT_UPDATE_CONTENT_VIEW_DATA(book, section, true));
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Active @Named(Const.CONTEXT_ACTIVE_VIEW_SECTION) BookSection section) {
		return section != null;
	}

}