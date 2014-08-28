package ebook.module.book.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.module.conf.tree.ContextInfoSelection;
import ebook.utils.Strings;

public class DeleteContext {
	@Execute
	public void execute(Shell shell, @Active BookConnection book,
			@Active ContextInfoSelection selection, @Active SectionInfo section) {

		if (!MessageDialog.openConfirm(shell, Strings.title("appTitle"),
				"Удалить контекст?"))
			return;

		book.ctxsrv(section).delete(selection);
	}

	@CanExecute
	public boolean canExecute(@Optional @Active ContextInfoSelection item,
			@Optional @Active SectionInfo section) {
		return item != null && !item.isEmpty() && section != null;
	}

}