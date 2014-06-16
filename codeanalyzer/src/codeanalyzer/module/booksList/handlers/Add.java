package codeanalyzer.module.booksList.handlers;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.module.booksList.IBookListManager;
import codeanalyzer.module.booksList.tree.ListBookInfo;
import codeanalyzer.module.cf.interfaces.ICf;
import codeanalyzer.utils.Strings;

public class Add {
	@Execute
	public void execute(@Optional ICf db, Shell shell, IBookListManager bm,
			@Optional ListBookInfo book) {

		InputDialog dlg = new InputDialog(shell,
				codeanalyzer.utils.Strings.get("appTitle"),
				"������� ��� ����� �����:", db == null ? "" : db.getName()
						.replaceAll("[//\\:\\.]", "_"), null);
		if (dlg.open() == Window.OK) {
			try {
				bm.addBook(dlg.getValue(), book);
			} catch (InvocationTargetException e) {
				MessageDialog
						.openError(shell, Strings.get("appTitle"),
								"������ �������� �����. \n��������, ����� � ����� ��������� ��� ����������.");
			}
		}

	}

}