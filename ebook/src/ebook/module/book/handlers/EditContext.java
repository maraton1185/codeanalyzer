package ebook.module.book.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.module.conf.tree.ContextInfo;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;

public class EditContext {
	@Execute
	public void execute(@Active BookConnection book,
			@Active SectionInfo section, @Active ContextInfo item) {
		// book.ctxsrv(section).delete(selection);
		App.br.post(Events.EVENT_UPDATE_CONTEXT_VIEW_EDIT_TITLE,
				new EVENT_UPDATE_VIEW_DATA(book, section, item, null));
	}

	@CanExecute
	public boolean canExecute(@Optional @Active ContextInfo item,
			@Optional @Active SectionInfo section) {
		return item != null && section != null;
	}

}