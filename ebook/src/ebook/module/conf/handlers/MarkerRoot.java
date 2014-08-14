package ebook.module.conf.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.module.conf.ConfConnection;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ListInfo;
import ebook.utils.Events;

public class MarkerRoot {

	@Execute
	public void execute(Shell shell, @Optional ContextInfo item,
			@Active ConfConnection con,
			@Active @Named(Events.CONTEXT_ACTIVE_LIST) ListInfo list) {

		try {
			item.getOptions().type = BuildType.root;
			con.srv(list).saveOptions(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@CanExecute
	public boolean canExecute(@Optional @Active ContextInfo item) {
		return item != null;
	}

}