package ebook.module.conf.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ListInfo;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class OpenInNewList {

	@Inject
	EHandlerService hs;
	@Inject
	ECommandService cs;
	@Inject
	@Active
	MWindow window;

	@Execute
	public void execute(Shell shell, @Optional ContextInfo item,
			@Active ConfConnection con) {

		ListInfo source_list = (ListInfo) con.lsrv().get(item.getList());
		ListInfo newList = App.mng.clm(con).openInNewList(con.srv(source_list),
				item, shell);

		if (newList != null) {
			window.getContext().set(ListInfo.class, newList);
			Utils.executeHandler(hs, cs, Strings.get("ListView.show"));
		}

	}

	@CanExecute
	public boolean canExecute(@Optional @Active ContextInfo item) {
		return item != null;
	}

}