package codeanalyzer.handlers.main;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import codeanalyzer.utils.Strings;

public class OpenBookList {
	@Execute
	public void execute(EPartService ps, EModelService model, MApplication app) {
		MPerspective persp = (MPerspective) model.find(
				Strings.get("model.id.perspective.books"), app);
		persp.setVisible(true);
		ps.switchPerspective(persp);
	}

}