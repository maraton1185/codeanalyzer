package ebook.module.text.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import ebook.core.App;
import ebook.module.text.TextConnection;
import ebook.module.text.model.HistoryItem;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class Previous {

	@Inject
	EHandlerService hs;
	@Inject
	ECommandService cs;

	@Execute
	public void execute(@Active TextConnection con) {
		HistoryItem item = App.getHistory().previous();
		if (item == null)
			return;
		con.setItem(item.getItem());
		con.setLine(item.getLine());
		Utils.executeHandler(hs, cs, Strings.model("TextView.show"));

	}

	@CanExecute
	public boolean canExecute(@Active @Optional TextConnection con) {
		return con != null && con.isValid() && con.isConf();
	}
}