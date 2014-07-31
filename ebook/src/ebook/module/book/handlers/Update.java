package ebook.module.book.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;

public class Update {
	@Execute
	public void execute(
			@Active BookConnection book,
			@Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {
		App.br.post(Events.EVENT_UPDATE_SECTION_VIEW,
				new EVENT_UPDATE_VIEW_DATA(book, section));
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {
		return section != null;
	}

}