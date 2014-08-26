package ebook.module.text.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import ebook.module.text.TextConnection;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class GoToModule {

	@Inject
	EHandlerService hs;
	@Inject
	ECommandService cs;

	@Execute
	public void execute(@Active TextConnection con) {

		con.setItem(con.getParent());
		Utils.executeHandler(hs, cs, Strings.model("TextView.show"));
	}

	@CanExecute
	public boolean canExecute(@Active @Optional TextConnection con) {
		return con.getParent() != null && con != null && con.isValid();
	}
}