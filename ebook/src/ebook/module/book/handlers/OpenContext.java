package ebook.module.book.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.confList.tree.ListConfInfo;

public class OpenContext {
	@Execute
	public void execute(final @Active BookConnection con,
			@Active final ContextInfo item, final Shell shell) {

		ContextInfoOptions opt = item.getOptions();
		if (opt.conf == null)
			return;
		ListConfInfo info = (ListConfInfo) App.srv.cl().getTreeItem(opt.conf);
		if (info == null)
			return;

		App.mng.clm().open(info.getPath(), shell);
	}

	@CanExecute
	public boolean canExecute(@Optional @Active ContextInfo item) {
		return item != null;
	}

}