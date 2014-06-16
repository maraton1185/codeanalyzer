package codeanalyzer.module.books.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import codeanalyzer.core.App;
import codeanalyzer.module.books.BookConnection;
import codeanalyzer.module.books.tree.SectionInfo;
import codeanalyzer.utils.Events;
import codeanalyzer.utils.Events.EVENT_UPDATE_VIEW_DATA;

public class EditTitle {
	@Execute
	public void execute(BookConnection book, @Active SectionInfo section) {

		App.br.post(Events.EVENT_EDIT_TITLE_CONTENT_VIEW,
				new EVENT_UPDATE_VIEW_DATA(book, section, null));

	}

	@CanExecute
	public boolean canExecute(@Optional @Active SectionInfo section) {
		return section != null;
	}

}