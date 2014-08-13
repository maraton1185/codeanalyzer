package ebook.module.book.handlers;

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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfoSelection;
import ebook.module.tree.ITreeItemInfo;
import ebook.utils.Strings;

public class Delete {

	@Inject
	@Active
	MWindow window;
	@Inject
	EPartService partService;
	@Inject
	EModelService model;

	@Execute
	public void execute(Shell shell, @Active BookConnection book,
			@Active SectionInfoSelection selection) {

		if (!MessageDialog.openConfirm(shell, Strings.get("appTitle"),
				"Удалить раздел?"))
			return;

		List<MPartStack> stacks = model
				.findElements(window, Strings.get("ebook.partstack.sections"),
						MPartStack.class, null);

		final Iterator<ITreeItemInfo> iterator = selection.iterator();
		while (iterator.hasNext()) {
			final ITreeItemInfo section = iterator.next();
			if (section.isRoot())
				continue;
			String partID = !section.isGroup() ? Strings
					.get("ebook.partdescriptor.sectionsBlockView") : Strings
					.get("ebook.partdescriptor.sectionView");

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

		book.srv().delete(selection);
	}

	@CanExecute
	public boolean canExecute(@Optional @Active SectionInfoSelection item) {
		return item != null && !item.isEmpty();
	}

}