package codeanalyzer.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class AddBookHandler {
	@Execute
	public void execute(Shell shell) {

		MessageDialog.openInformation(shell, "", "создать книгу");
		// MPart part = partService.createPart(Strings
		// .get("codeanalyzer.partdescriptor.treeView"));
		//
		// if (db != null)
		// part.setLabel(db.getName());
		// // else
		// // part.setLabel(db.getName());
		//
		// List<MPartStack> stacks = model.findElements(AppManager.app,
		// Strings.get("model.id.partstack.tree"), MPartStack.class, null);
		// stacks.get(0).getChildren().add(part);
		//
		// partService.showPart(part, PartState.ACTIVATE);
	}

	// @CanExecute
	// public boolean canExecute(@Optional @Named(Const.CONTEXT_SELECTED_DB) IDb
	// db) {
	// return db != null;
	// }
}