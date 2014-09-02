package ebook.module.text.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import ebook.core.App;
import ebook.module.text.TextConnection;
import ebook.module.text.tree.BookmarkInfo;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;

public class BmrkEditTitle {

	@Execute
	public void execute(@Active BookmarkInfo item, @Active TextConnection con) {

		App.br.post(Events.EVENT_UPDATE_BOOKMARK_VIEW_EDIT_TITLE,
				new EVENT_UPDATE_VIEW_DATA(con.getCon(), item, null));
	}

	@CanExecute
	public boolean canExecute(@Optional @Active BookmarkInfo item) {
		return item != null;
	}
}
