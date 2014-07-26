package ebook.handlers;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import ebook.core.App;
import ebook.core.App.Perspectives;
import ebook.utils.Events;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class About {

	@Inject
	@Optional
	public void SHOW_ABOUT(@UIEventTopic(Events.SHOW_ABOUT) Object o,
			final EHandlerService hs, final ECommandService cs) {

		Utils.executeHandler(hs, cs, Strings.get("command.id.ShowAbout"));
	}

	@Execute
	public void execute(EModelService model, @Active MWindow window,
			EPartService ps) {

		String id = App.currentPerspective == Perspectives.main ? Strings
				.get("placeholder1.ABOUT") : Strings.get("placeholder2.ABOUT");

		List<MPlaceholder> phs = model.findElements(window, id,
				MPlaceholder.class, null);
		if (!phs.isEmpty()) {
			MPlaceholder ph = phs.get(0);
			ph.setVisible(true);

			id = Strings.get("part.ABOUT");

			List<MPart> parts = model.findElements(window, id, MPart.class,
					null);
			if (!parts.isEmpty()) {
				MPart part = parts.get(0);
				// part.setVisible(true);
				ps.showPart(part, PartState.VISIBLE);
			}
		}

	}

}