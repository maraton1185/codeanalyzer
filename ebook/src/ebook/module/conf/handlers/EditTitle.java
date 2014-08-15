package ebook.module.conf.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import ebook.core.App;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ListInfo;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;

public class EditTitle {

	@Execute
	public void execute(@Active ContextInfo item, @Active ConfConnection con,
			@Active @Named(Events.CONTEXT_ACTIVE_LIST) ListInfo list) {

		App.br.post(Events.EVENT_UPDATE_CONF_VIEW_EDIT_TITLE,
				new EVENT_UPDATE_VIEW_DATA(con, list, item, null));
	}

	@CanExecute
	public boolean canExecute(@Optional @Active ContextInfo item) {
		return item != null;
	}
}
