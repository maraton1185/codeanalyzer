package ebook.module.userList.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.userList.tree.UserInfo;
import ebook.utils.Strings;

public class FilterSet {
	@Execute
	public void execute(@Optional UserInfo item, Shell shell) {
		if (item == null)
			return;
		if (!item.isGroup()) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"������ ����� ������������� ������ �� ������.");
			return;
		}
		try {
			App.srv.us().setRoot(item);
		} catch (Exception e) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"������ ��������� �������.");
		}
	}
}