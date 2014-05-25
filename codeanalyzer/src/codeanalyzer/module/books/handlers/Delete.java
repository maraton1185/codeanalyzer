package codeanalyzer.module.books.handlers;

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

import codeanalyzer.module.books.BookConnection;
import codeanalyzer.module.books.tree.SectionInfo;
import codeanalyzer.module.books.tree.SectionInfoSelection;
import codeanalyzer.utils.Strings;

public class Delete {
	@Execute
	public void execute(Shell shell, BookConnection book,
			@Active final SectionInfo section, SectionInfoSelection selection,
			EPartService partService, EModelService model,
			@Active MWindow window) {
		// if (MessageDialog.openConfirm(shell, Strings.get("appTitle"),
		// "Удалить раздел?"))
		book.srv().delete(selection);

		List<MPartStack> stacks = model.findElements(window,
				Strings.get("model.id.partstack.sections"), MPartStack.class,
				null);

		String partID = !section.isGroup() ? Strings
				.get("codeanalyzer.partdescriptor.sectionsBlockView") : Strings
				.get("codeanalyzer.partdescriptor.sectionView");

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
	public boolean canExecute(@Optional @Active SectionInfo section) {
		return section != null;
	}

}