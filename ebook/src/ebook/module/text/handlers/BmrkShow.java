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
import ebook.module.text.tree.BookmarkInfo;

public class BmrkShow {

	@Inject
	EPartService partService;
	@Inject
	EModelService model;

	@Execute
	public void execute(@Active BookmarkInfo item, @Active TextConnection con,
			@Active MWindow window) {

		con.setItem(item.getItem());
		con.setLine(item.getLine());
		Show.show(window, model, partService, con);
	}

	@CanExecute
	public boolean canExecute(@Optional @Active BookmarkInfo item) {
		return item != null;
	}
}
