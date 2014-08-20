package ebook.module.conf.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

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

import ebook.module.conf.tree.ListInfo;
import ebook.utils.Strings;

public class Show {

	@Execute
	public void execute(@Active final ListInfo list, Shell shell,
			EPartService partService, EModelService model,
			@Active MWindow window,
			@Optional @Named("ebook.commandparameter.dirty") String dirty) {

		List<MPartStack> stacks = model.findElements(window,
				Strings.model("ebook.partstack.conf"), MPartStack.class, null);

		String partID = Strings.model("ebook.partdescriptor.0");

		stacks.get(0).setVisible(true);

		@SuppressWarnings("serial")
		List<MPart> parts = model.findElements(stacks.get(0), partID,
				MPart.class, new ArrayList<String>() {
					{
						add(list.getId().toString());
					}
				});

		MPart part;

		if (parts.isEmpty()) {
			part = partService.createPart(partID);

			part.setLabel(list.getTitle());
			part.getTags().add(list.getId().toString());
			stacks.get(0).getChildren().add(part);

			if (dirty != null)
				part.setDirty(true);
		} else {
			part = parts.get(0);
			part.setLabel(list.getTitle());
		}

		partService.showPart(part, PartState.VISIBLE);
	}

	@CanExecute
	public boolean canExecute(@Active @Optional ListInfo list) {

		return list != null;
	}

}