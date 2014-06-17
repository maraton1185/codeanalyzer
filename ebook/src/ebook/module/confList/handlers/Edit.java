package ebook.module.confList.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import ebook.core.App;
import ebook.module.confList.tree.ListConfInfo;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_TREE_DATA;

public class Edit {
	@Execute
	public void execute(ListConfInfo item) {
		App.br.post(Events.EVENT_EDIT_TITLE_CONF_LIST,
				new EVENT_UPDATE_TREE_DATA(null, item));
	}

	@CanExecute
	public boolean canExecute(@Optional @Active ListConfInfo item) {
		return item != null;
	}

}