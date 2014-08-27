package ebook.module.text.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import ebook.core.App;
import ebook.module.text.TextConnection;
import ebook.module.text.model.LineInfo;
import ebook.utils.Events;

public class GoToModule {

	@Inject
	EHandlerService hs;
	@Inject
	ECommandService cs;

	@Execute
	public void execute(
			@Active @Optional @Named(Events.TEXT_VIEW_ACTIVE_PROCEDURE) LineInfo item,
			@Active TextConnection con) {
		con.setLine(item);
		con.setItem(con.getParent());
		// Utils.executeHandler(hs, cs, Strings.model("TextView.show"));
		App.br.post(Events.EVENT_SHOW_TEXT, null);
	}

	@CanExecute
	public boolean canExecute(@Active @Optional TextConnection con) {
		return con != null && con.getParent() != null && con.isValid();
	}
}