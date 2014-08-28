package ebook.module.text.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.IEclipseContext;
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

import ebook.module.text.TextConnection;
import ebook.utils.Strings;

public class Show {

	@Inject
	@Optional
	Shell shell;
	@Inject
	EPartService partService;
	@Inject
	EModelService model;
	@Inject
	EHandlerService hs;
	@Inject
	ECommandService cs;
	@Inject
	IEclipseContext ctx;

	@Execute
	public void execute(@Active MWindow window, @Active final TextConnection con) {

		show(window, model, partService, con);

	}

	public static void show(MWindow window, EModelService model,
			EPartService partService, final TextConnection con) {
		// App.br.post(Events.EVENT_SHOW_TEXT, null);
		List<MPartStack> stacks = model.findElements(window,
				Strings.model("ebook.partstack.text"), MPartStack.class, null);

		String partID = Strings.model("ebook.partdescriptor.1");

		stacks.get(0).setVisible(true);

		@SuppressWarnings("serial")
		List<MPart> parts = model.findElements(stacks.get(0), partID,
				MPart.class, new ArrayList<String>() {
					{
						add(con.getItem().getId().toString());
					}
				});

		MPart part;

		if (parts.isEmpty()) {
			part = partService.createPart(partID);

			part.setLabel(con.getItem().getTitle());
			part.getTags().add(con.getItem().getId().toString());
			stacks.get(0).getChildren().add(part);

		} else {
			part = parts.get(0);
			part.setLabel(con.getItem().getTitle());
		}

		partService.showPart(part, PartState.VISIBLE);

	}

	@CanExecute
	public boolean canExecute(@Active @Optional TextConnection con) {

		return con != null && con.isValid();
	}

}