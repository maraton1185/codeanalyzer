package ebook.module.conf.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.tree.ContextInfoSelection;
import ebook.module.conf.tree.ListInfo;
import ebook.utils.Events;

public class Delete {

	@Execute
	public void execute(Shell shell, @Optional ContextInfoSelection selection,
			@Active ConfConnection con,
			@Active @Named(Events.CONTEXT_ACTIVE_LIST) final ListInfo list) {

		App.mng.cm(con, list).delete(selection, shell);

	}

	@CanExecute
	public boolean canExecute(@Optional @Active ContextInfoSelection selection) {
		return selection != null;
	}
}
