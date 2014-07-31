package ebook.module.conf.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.tree.ContextInfo;

public class AddSubGroup {

	@Execute
	public void execute(Shell shell, @Optional ContextInfo item,
			@Active ConfConnection con) {

		App.mng.cm(con).addSubGroup(item, shell);

	}

	@CanExecute
	public boolean canExecute(@Optional @Active ContextInfo item) {
		return item != null;
	}
}
