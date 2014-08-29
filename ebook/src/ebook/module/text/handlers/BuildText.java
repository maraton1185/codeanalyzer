package ebook.module.text.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import ebook.module.text.TextConnection;

public class BuildText {
	@Execute
	public void execute(@Active MPart part) {
		// if (part.getObject() instanceof TextView)
		// ((TextView) part.getObject()).Collapse();
	}

	@CanExecute
	public boolean canExecute(@Active @Optional TextConnection con) {
		return con != null && con.isValid() && con.isConf();
	}
}