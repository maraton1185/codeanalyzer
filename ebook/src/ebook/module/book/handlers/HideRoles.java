package ebook.module.book.handlers;

import java.util.List;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;

import ebook.utils.Strings;

public class HideRoles {

	// @Inject
	// @Optional
	// public void EVENT_HIDE_BOOK_ROLES(
	// @UIEventTopic(Events.EVENT_HIDE_BOOK_ROLES) Object o,
	// final EHandlerService hs, final ECommandService cs) {
	//
	// Utils.executeHandler(hs, cs,
	// Strings.get("command.id.EVENT_HIDE_BOOK_ROLES"));
	// }

	@Execute
	public void execute(Shell shell, EPartService partService,
			EModelService model, @Active MWindow window) {

		String partID = Strings.model("part.SectionRolesView");

		List<MPart> parts = model.findElements(window, partID, MPart.class,
				null);

		MPart part;

		if (!parts.isEmpty()) {
			part = parts.get(0);
			partService.hidePart(part);
		}

	}

}
