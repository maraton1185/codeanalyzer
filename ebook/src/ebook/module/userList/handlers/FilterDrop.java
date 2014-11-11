package ebook.module.userList.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.utils.Strings;

public class FilterDrop {
	@Execute
	public void execute(Shell shell) {
		try {
			App.srv.us().dropRoot();
		} catch (Exception e) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"������ ������ �������.");
		}

	}

}