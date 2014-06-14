package ebook.module.bookList.handlers;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import ebook.module.bookList.IBookListManager;
import ebook.module.bookList.tree.ListBookInfo;
import ebook.module.confList.tree.ListConfInfo;
import ebook.utils.Strings;

public class Add {
	@Execute
	public void execute(@Optional ListConfInfo db, Shell shell,
			IBookListManager mng, @Optional ListBookInfo book) {

		InputDialog dlg = new InputDialog(shell,
				ebook.utils.Strings.get("appTitle"),
				"������� ��� ����� �����:", db == null ? "" : db.getTitle()
						.replaceAll("[//\\:\\.]", "_"), null);
		if (dlg.open() == Window.OK) {
			try {
				mng.add(dlg.getValue(), book);
			} catch (InvocationTargetException e) {
				MessageDialog
						.openError(shell, Strings.get("appTitle"),
								"������ �������� �����. \n��������, ����� � ����� ��������� ��� ����������.");
			}
		}

	}

}