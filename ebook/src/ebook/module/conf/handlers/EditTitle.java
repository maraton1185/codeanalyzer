package ebook.module.conf.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import ebook.core.App;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.tree.ContextInfo;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;

public class EditTitle {

	@Execute
	public void execute(ContextInfo item, @Active ConfConnection con) {

		App.br.post(Events.EVENT_EDIT_TITLE_CONF_VIEW,
				new EVENT_UPDATE_VIEW_DATA(con, item, null));
	}

	@CanExecute
	public boolean canExecute(@Optional @Active ContextInfo item) {
		return item != null;
	}
}
