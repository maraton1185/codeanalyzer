package ebook.module.confList.handlers;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import ebook.module.conf.ConfConnection;
import ebook.module.confList.tree.ListConfInfo;
import ebook.utils.Strings;

public class CanLoad {

	@Inject
	Shell shell;

	@Execute
	public void execute(ListConfInfo item) {
		InputDialog dlg = new InputDialog(shell,
				ebook.utils.Strings.title("appTitle"), "¬ведите пароль:", null,
				null);
		if (dlg.open() == Window.OK) {
			try {
				ConfConnection con = new ConfConnection(item.getPath());

				con.srv(null).setPassword(dlg.getValue());

			} catch (InvocationTargetException e) {
				MessageDialog.openError(shell, Strings.title("appTitle"),
						e.getMessage());
			}
		}
	}

	@CanExecute
	public boolean canExecute(@Optional ListConfInfo item) {
		return item != null && !item.isGroup();
	}
}