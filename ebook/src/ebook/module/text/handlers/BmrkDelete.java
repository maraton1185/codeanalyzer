package ebook.module.text.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.module.text.TextConnection;
import ebook.module.text.tree.BookmarkInfoSelection;

public class BmrkDelete {

	@Execute
	public void execute(Shell shell, @Optional BookmarkInfoSelection selection,
			@Active TextConnection con) {

		con.bmkSrv().delete(selection);

	}

	@CanExecute
	public boolean canExecute(@Optional @Active BookmarkInfoSelection selection) {
		return selection != null && !selection.isEmpty();
	}
}
