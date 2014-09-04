package ebook.module.conf.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

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
import ebook.module.conf.tree.ListInfoSelection;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.utils.Strings;

public class ListDelete {

	@Inject
	@Active
	MWindow window;
	@Inject
	EPartService partService;
	@Inject
	EModelService model;

	@Execute
	public void execute(Shell shell, @Active ConfConnection con,
			@Optional ListInfoSelection selection) {

		List<MPartStack> stacks = model.findElements(window,
				Strings.model("ebook.partstack.conf"), MPartStack.class, null);

		String partID = Strings.model("ebook.partdescriptor.0");

		final Iterator<ITreeItemInfo> iterator = selection.iterator();
		while (iterator.hasNext()) {
			final ITreeItemInfo section = iterator.next();
			if (section.isRoot())
				continue;
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
		App.mng.clm(con).delete(selection, shell);
	}

	@CanExecute
	public boolean canExecute(@Optional @Active ListInfoSelection selection) {
		return selection != null && !selection.isEmpty();
	}
}
