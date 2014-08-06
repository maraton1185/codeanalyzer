package ebook.module.conf.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.tree.ListInfo;
import ebook.module.conf.tree.ListInfoSelection;
import ebook.utils.Strings;

public class ListDelete {

	@Execute
	public void execute(Shell shell, @Active ConfConnection con,
			@Active final ListInfo section,
			@Optional ListInfoSelection selection, EPartService partService,
			EModelService model, @Active MWindow window) {

		// }
		// @Execute
		// public void execute(Shell shell, @Optional ListInfoSelection
		// selection,
		// @Active ConfConnection con) {

		App.mng.clm(con).delete(selection, shell);

		List<MPartStack> stacks = model.findElements(window,
				Strings.get("ebook.partstack.conf"), MPartStack.class, null);

		String partID = Strings.get("ebook.partdescriptor.0");

		@SuppressWarnings("serial")
		List<MPart> parts = model.findElements(stacks.get(0), partID,
				MPart.class, new ArrayList<String>() {
					{
						add(section.getId().toString());
					}
				});

		if (!parts.isEmpty()) {
			MPart part = parts.get(0);
			partService.hidePart(part, true);
		}
	}

	@CanExecute
	public boolean canExecute(@Optional @Active ListInfoSelection selection) {
		return selection != null;
	}
}
