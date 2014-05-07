package codeanalyzer.handlers.main;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

public class SetTitle {
	@Execute
	public void execute(MPart activePart, Shell shell) {
		InputDialog dlg = new InputDialog(shell,
				codeanalyzer.utils.Strings.get("appTitle"),
				"¬ведите название области:", activePart.getLabel(), null);
		if (dlg.open() == Window.OK) {
			activePart.setLabel(dlg.getValue());
		}
	}
}