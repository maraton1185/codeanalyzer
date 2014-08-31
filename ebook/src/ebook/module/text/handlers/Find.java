package ebook.module.text.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import ebook.core.App;
import ebook.module.text.TextConnection;
import ebook.utils.Events;

public class Find {

	// @Execute
	// public void execute(Shell shell, FindDialog dlg, @Active TextConnection
	// con) {
	// dlg.open();
	// }

	@Execute
	public void execute(@Active TextConnection con) {
		App.br.post(Events.EVENT_TEXT_VIEW_FIND_TEXT, con.getItem());
	}

	@CanExecute
	public boolean canExecute(@Active @Optional TextConnection con) {
		return con != null && con.isValid() && con.isConf();
	}

}
