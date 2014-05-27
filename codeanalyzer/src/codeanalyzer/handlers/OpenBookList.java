package codeanalyzer.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import codeanalyzer.core.App;
import codeanalyzer.core.App.Perspectives;

public class OpenBookList {
	@Execute
	public void execute(EPartService ps, EModelService model, MApplication app) {

		App.showPerspective(Perspectives.books);

	}

}