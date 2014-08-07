package ebook.module.book.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.module.conf.tree.ContextInfoSelection;

public class DeleteContext {
	@Execute
	public void execute(@Active BookConnection book,
			@Active ContextInfoSelection selection, @Active SectionInfo section) {
		book.ctxsrv(section).delete(selection);
	}

	@CanExecute
	public boolean canExecute(@Optional @Active ContextInfoSelection item,
			@Optional @Active SectionInfo section) {
		return item != null && section != null;
	}

}