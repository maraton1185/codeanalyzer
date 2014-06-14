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
import ebook.module.cf.interfaces.ICf;
import ebook.utils.Strings;

public class AddGroup {
	@Execute
	public void execute(@Optional ICf db, Shell shell, IBookListManager bm,
			@Optional ListBookInfo book) {
		InputDialog dlg = new InputDialog(shell,
				ebook.utils.Strings.get("appTitle"),
				"¬ведите название группы:", "", null);
		if (dlg.open() == Window.OK)
			try {

				ListBookInfo data = new ListBookInfo();
				data.setTitle(dlg.getValue());
				data.setGroup(true);
				bm.addGroup(data, book, false);

				// bm.add((ITreeItemInfo) data);

			} catch (InvocationTargetException e) {
				MessageDialog.openError(shell, Strings.get("appTitle"),
						"ќшибка создании группы.");
			}

	}

}