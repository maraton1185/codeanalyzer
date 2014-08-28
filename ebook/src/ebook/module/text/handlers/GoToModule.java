package ebook.module.text.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import ebook.module.text.TextConnection;
import ebook.module.text.model.LineInfo;
import ebook.utils.Events;

public class GoToModule {

	@Inject
	EHandlerService hs;
	@Inject
	ECommandService cs;
	@Inject
	EPartService partService;
	@Inject
	EModelService model;

	@Execute
	public void execute(
			@Active @Optional @Named(Events.TEXT_VIEW_ACTIVE_PROCEDURE) LineInfo item,
			@Active TextConnection con, @Active MWindow window) {
		con.setLine(item);
		con.setItem(con.getParent());
		Show.show(window, model, partService, con);
	}

	@CanExecute
	public boolean canExecute(@Active @Optional TextConnection con) {
		return con != null && con.getParent() != null && con.isValid();
	}
}