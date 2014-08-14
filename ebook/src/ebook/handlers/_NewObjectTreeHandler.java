package ebook.handlers;

import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import ebook.core.App;
import ebook.module.confList.tree.ListConfInfo;
import ebook.utils.Strings;

public class _NewObjectTreeHandler {
	@Execute
	public void execute(@Optional ListConfInfo db, EPartService partService,
			EModelService model) {

		MPart part = partService.createPart(Strings
				.model("ebook.partdescriptor.treeView"));

		if (db != null)
			part.setLabel(db.getName());
		// else
		// part.setLabel(db.getName());

		List<MPartStack> stacks = model.findElements(App.app,
				Strings.model("model.id.partstack.tree"), MPartStack.class,
				null);
		stacks.get(0).getChildren().add(part);

		partService.showPart(part, PartState.ACTIVATE);
	}

}