package ebook.module.book.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;

public class AddSectionLink {

	@Execute
	public void execute(@Active SectionInfo item, @Active BookConnection book) {
		App.br.post(Events.EVENT_ADD_SECTION_LINK, new EVENT_UPDATE_VIEW_DATA(
				book, book.srv().get(item.getParent()), item));

	}

	@CanExecute
	public boolean canExecute(@Active @Optional SectionInfo item) {
		return item != null;
	}

}