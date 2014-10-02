package ebook.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import ebook.dialogs.ContactDialog;

public class Contact {
	@Execute
	public void execute(Shell shell, ContactDialog dlg) {
		dlg.open();
	}

}