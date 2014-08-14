package ebook.module.confList.handlers;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.module.conf.ConfConnection;
import ebook.module.confList.tree.ListConfInfo;
import ebook.utils.Strings;

public class Unlock {
	@Execute
	public void execute(ListConfInfo item, Shell shell) {
		try {
			ConfConnection con = new ConfConnection(item.getPath(), false);
			con.closeConnection();
			MessageDialog.openInformation(shell, Strings.title("appTitle"),
					"Файл конфигурации разблокирован.");
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Ошибка операции. Перезайдите в программу.");
		}
	}

	@CanExecute
	public boolean canExecute(@Optional @Active ListConfInfo item) {
		return item != null && !item.isGroup();
	}

}