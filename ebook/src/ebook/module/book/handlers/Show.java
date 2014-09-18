package ebook.module.book.handlers;

import java.util.ArrayList;
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
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.program.Program;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.utils.Events;
import ebook.utils.Strings;

public class Show {

	@Inject
	@Active
	BookConnection book;

	@Inject
	@Active
	MWindow window;

	@Inject
	EPartService partService;
	@Inject
	EModelService model;

	@Execute
	public void execute(@Active final SectionInfo section) {

		if (section.isGroup()) {
			String url = App.getJetty().host()
					+ App.getJetty().section(book.getTreeItem().getId(),
							section.getId());
			Program.launch(url);
			App.br.post(Events.EVENT_SET_SECTION_CONTEXT, null);
			return;
		}

		List<MPartStack> stacks = model.findElements(window,
				Strings.model("model.id.partstack.sections"), MPartStack.class,
				null);

		String partID = Strings.model("ebook.partdescriptor.sectionsBlockView");

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

		partService.showPart(part, PartState.VISIBLE);
	}

	@CanExecute
	public boolean canExecute(@Active @Optional SectionInfo section) {

		return section != null && App.getJetty().isStarted();
	}

}