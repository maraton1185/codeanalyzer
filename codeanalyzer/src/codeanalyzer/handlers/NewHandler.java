 
package codeanalyzer.handlers;

import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import codeanalyzer.core.AppManager;

public class NewHandler {
	@Execute
	public void execute(EPartService partService, EModelService model) {
		MPart part = partService
				.createPart("codeanalyzer.partdescriptor.treeView");
		part.setLabel("test");

		List<MPartStack> stacks = model.findElements(AppManager.app,
				"codeanalyzer.partstack.treeView",
				MPartStack.class, null);
		stacks.get(0).getChildren().add(part);

		partService.showPart(part, PartState.ACTIVATE);
	}
		
}