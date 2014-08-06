package ebook.module.conf.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import ebook.core.App;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.tree.ListInfo;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;

public class ListEditTitle {

	@Execute
	public void execute(ListInfo item, @Active ConfConnection con) {

		App.br.post(Events.EVENT_EDIT_TITLE_LIST_VIEW,
				new EVENT_UPDATE_VIEW_DATA(con, item, null));
	}

	@CanExecute
	public boolean canExecute(@Optional @Active ListInfo item) {
		return item != null;
	}
}
