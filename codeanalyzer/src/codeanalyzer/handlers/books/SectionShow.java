package codeanalyzer.handlers.books;

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
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.book.BookSection;
import codeanalyzer.utils.Strings;

public class SectionShow {
	@Execute
	public void execute(@Active final BookSection section, Shell shell,
			EPartService partService, EModelService model,
			@Active MWindow window) {

		List<MPartStack> stacks = model.findElements(window,
				Strings.get("model.id.partstack.sections"), MPartStack.class,
				null);

		String partID = section.block ? Strings
				.get("codeanalyzer.partdescriptor.sectionsBlockView") : Strings
				.get("codeanalyzer.partdescriptor.sectionView");

		@SuppressWarnings("serial")
		List<MPart> parts = model.findElements(stacks.get(0), partID,
				MPart.class, new ArrayList<String>() {
					{
						add(section.id.toString());
					}
				});

		MPart part;

		if (parts.isEmpty()) {
			part = partService.createPart(partID);

			part.setLabel(section.title);
			part.getTags().add(section.id.toString());
			stacks.get(0).getChildren().add(part);
		} else {
			part = parts.get(0);
		}

		partService.showPart(part, PartState.ACTIVATE);
	}

	@CanExecute
	public boolean canExecute(@Optional BookSection section) {

		return section != null;
	}

}