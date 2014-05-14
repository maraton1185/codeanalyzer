package codeanalyzer.handlers.main;

import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import codeanalyzer.cf.interfaces.ICf;
import codeanalyzer.core.AppManager;
import codeanalyzer.utils.Strings;

public class NewObjectTreeHandler {
	@Execute
	public void execute(@Optional ICf db, EPartService partService,
			EModelService model) {

		MPart part = partService.createPart(Strings
				.get("codeanalyzer.partdescriptor.treeView"));

		if (db != null)
			part.setLabel(db.getName());
		// else
		// part.setLabel(db.getName());

		List<MPartStack> stacks = model.findElements(AppManager.app,
				Strings.get("model.id.partstack.tree"), MPartStack.class, null);
		stacks.get(0).getChildren().add(part);

		partService.showPart(part, PartState.ACTIVATE);
	}

}