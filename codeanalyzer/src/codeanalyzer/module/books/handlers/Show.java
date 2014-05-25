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
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.module.books.tree.SectionInfo;
import codeanalyzer.utils.Strings;

public class Show {
	@Execute
	public void execute(@Active final SectionInfo section, Shell shell,
			EPartService partService, EModelService model,
			@Active MWindow window) {

		List<MPartStack> stacks = model.findElements(window,
				Strings.get("model.id.partstack.sections"), MPartStack.class,
				null);

		String partID = !section.isGroup() ? Strings
				.get("codeanalyzer.partdescriptor.sectionsBlockView") : Strings
				.get("codeanalyzer.partdescriptor.sectionView");

		stacks.get(0).setVisible(true);

		@SuppressWarnings("serial")
		List<MPart> parts = model.findElements(stacks.get(0), partID,
				MPart.class, new ArrayList<String>() {
					{
						add(section.getId().toString());
					}
				});

		MPart part;

		if (parts.isEmpty()) {
			part = partService.createPart(partID);

			part.setLabel(section.getTitle());
			part.getTags().add(section.getId().toString());
			stacks.get(0).getChildren().add(part);
		} else {
			part = parts.get(0);
			part.setLabel(section.getTitle());
		}

		partService.showPart(part, PartState.ACTIVATE);
	}

	@CanExecute
	public boolean canExecute(@Optional SectionInfo section) {

		return section != null;
	}

}