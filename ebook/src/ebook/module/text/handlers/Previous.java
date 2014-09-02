package ebook.module.text.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import ebook.module.text.TextConnection;
import ebook.module.text.model.History;
import ebook.module.text.model.HistoryItem;

public class Previous {

	@Inject
	EPartService partService;
	@Inject
	EModelService model;
	@Inject
	@Active
	History history;

	@Execute
	public void execute(@Active TextConnection con, @Active MWindow window) {
		HistoryItem item = history.previous();
		if (item == null)
			return;
		con.setItem(item.getItem());
		con.setLine(item.getLine());
		Show.show(window, model, partService, con);

	}

	@CanExecute
	public boolean canExecute(@Active @Optional TextConnection con) {
		return con != null && con.isValid();
	}
}