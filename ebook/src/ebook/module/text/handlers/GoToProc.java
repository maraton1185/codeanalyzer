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
import ebook.module.conf.tree.ContextInfo;
import ebook.module.text.TextConnection;
import ebook.module.text.model.LineInfo;
import ebook.module.tree.ITreeItemInfo;
import ebook.utils.Events;

public class GoToProc {

	@Inject
	EHandlerService hs;
	@Inject
	ECommandService cs;

	@Execute
	public void execute(
			@Active @Named(Events.TEXT_VIEW_ACTIVE_PROCEDURE) LineInfo item,
			@Active TextConnection con) {
		ContextInfo selected = con.srv().getItemByTitle(item);
		if (selected == null)
			return;
		con.setItem(selected);
		// Utils.executeHandler(hs, cs, Strings.model("TextView.show"));
		App.br.post(Events.EVENT_SHOW_TEXT, null);
	}

	@CanExecute
	public boolean canExecute(
			@Active @Optional @Named(Events.TEXT_VIEW_ACTIVE_PROCEDURE) ITreeItemInfo item,
			@Active @Optional TextConnection con) {
		return item != null && con != null && con.isValid();
	}
}