package codeanalyzer.handlers.books.section;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import codeanalyzer.books.book.CurrentBookInfo;
import codeanalyzer.books.section.SectionInfo;
import codeanalyzer.core.AppManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_VIEW_DATA;

public class Update {
	@Execute
	public void execute(
			CurrentBookInfo book,
			@Active @Named(Const.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {
		AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW,
				new EVENT_UPDATE_VIEW_DATA(book, section, true));
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Active @Named(Const.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {
		return section != null;
	}

}