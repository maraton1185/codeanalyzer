package codeanalyzer.handlers;

import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;

public class OpenStartPage {
	@Execute
	public void execute(EPartService ps, EModelService model, MApplication app) {
		MPerspective persp = (MPerspective) model.find(
				Strings.get("model.id.perspective.default"), app);
		persp.setVisible(true);
		ps.switchPerspective(persp);

		List<MPart> parts = model.findElements(app,
				Strings.get("model.id.part.start"), MPart.class, null);
		MPart part = parts.get(0);
		if (PreferenceSupplier.getBoolean(PreferenceSupplier.SHOW_START_PAGE))
			ps.showPart(part, PartState.ACTIVATE);
		else
			ps.hidePart(part);
	}

}