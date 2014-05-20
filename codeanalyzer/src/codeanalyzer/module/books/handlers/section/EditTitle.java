package codeanalyzer.module.books.handlers.section;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import codeanalyzer.core.AppManager;
import codeanalyzer.core.Events;
import codeanalyzer.core.Events.EVENT_UPDATE_VIEW_DATA;
import codeanalyzer.module.books.list.CurrentBookInfo;
import codeanalyzer.module.books.section.SectionInfo;

public class EditTitle {
	@Execute
	public void execute(CurrentBookInfo book, @Active SectionInfo section) {

		AppManager.br.post(Events.EVENT_EDIT_TITLE_CONTENT_VIEW,
				new EVENT_UPDATE_VIEW_DATA(book, section, null));

	}

	@CanExecute
	public boolean canExecute(@Optional @Active SectionInfo section) {
		return section != null;
	}

}