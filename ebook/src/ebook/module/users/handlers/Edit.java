 
package ebook.module.users.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import ebook.core.App;
import ebook.module.users.tree.UserInfo;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_TREE_DATA;

public class Edit {
	@Execute
	public void execute(UserInfo user) {

		App.br.post(Events.EVENT_EDIT_TITLE_USERS_LIST,
				new EVENT_UPDATE_TREE_DATA(null, user));

	}

	@CanExecute
	public boolean canExecute(@Optional @Active UserInfo user) {
		return user != null;
	}
		
}