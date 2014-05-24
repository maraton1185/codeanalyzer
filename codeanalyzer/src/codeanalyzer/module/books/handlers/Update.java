package codeanalyzer.module.books.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import codeanalyzer.core.App;
import codeanalyzer.module.books.BookConnection;
import codeanalyzer.module.books.section.SectionInfo;
import codeanalyzer.utils.Events;
import codeanalyzer.utils.Events.EVENT_UPDATE_VIEW_DATA;

public class Update {
	@Execute
	public void execute(
			BookConnection book,
			@Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {
		App.br.post(Events.EVENT_UPDATE_CONTENT_VIEW,
				new EVENT_UPDATE_VIEW_DATA(book, section, true));
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {
		return section != null;
	}

}