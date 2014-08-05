package ebook.module.book.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.module.book.BookConnection;
import ebook.module.conf.tree.ContextInfo;

public class OpenContext {
	@Execute
	public void execute(final @Active BookConnection con,
			@Active final ContextInfo item, final Shell shell) {

		System.out.println(item.getOptions().conf);
	}

	@CanExecute
	public boolean canExecute(@Optional @Active ContextInfo item) {
		return item != null;
	}

}