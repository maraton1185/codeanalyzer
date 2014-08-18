package ebook.module.book.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.tree.SectionInfoOptions;
import ebook.module.conf.tree.ContextInfoSelection;
import ebook.module.confList.tree.ListConfInfo;

public class OpenContext {
	@Execute
	public void execute(final @Active BookConnection con,
			@Active final SectionInfo section,
			@Active final ContextInfoSelection sel, final Shell shell) {

		SectionInfoOptions opt = section.getOptions();
		if (!opt.hasContext())
			return;
		ListConfInfo info = (ListConfInfo) App.srv.cl().getTreeItem(
				opt.getContextName(), "");
		if (info == null)
			return;

		// App.mng.clm().open(info.getPath(), shell);

		App.mng.clm().openWithContext(con.ctxsrv(section), info.getPath(), sel,
				shell);

	}

	@CanExecute
	public boolean canExecute(@Optional @Active ContextInfoSelection sel) {
		return sel != null && !sel.isEmpty();
	}

}