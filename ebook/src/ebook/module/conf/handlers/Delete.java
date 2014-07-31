package ebook.module.conf.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.tree.ContextInfoSelection;

public class Delete {

	@Execute
	public void execute(Shell shell, @Optional ContextInfoSelection selection,
			@Active ConfConnection con) {

		App.mng.cm(con).delete(selection, shell);

	}

	@CanExecute
	public boolean canExecute(@Optional @Active ContextInfoSelection selection) {
		return selection != null;
	}
}
