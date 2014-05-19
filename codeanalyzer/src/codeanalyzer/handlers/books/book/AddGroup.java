package codeanalyzer.handlers.books.book;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.books.interfaces.IBookManager;
import codeanalyzer.cf.interfaces.ICf;
import codeanalyzer.core.model.BookInfo;
import codeanalyzer.utils.Strings;

public class AddGroup {
	@Execute
	public void execute(@Optional ICf db, Shell shell, IBookManager bm,
			@Optional BookInfo book) {
		InputDialog dlg = new InputDialog(shell,
				codeanalyzer.utils.Strings.get("appTitle"),
				"¬ведите название группы:", "", null);
		if (dlg.open() == Window.OK)
			try {

				BookInfo data = new BookInfo();
				data.title = dlg.getValue();
				data.isGroup = true;
				data.path = "";
				bm.addGroup(data, book, false);

				// bm.add((ITreeItemInfo) data);

			} catch (InvocationTargetException e) {
				MessageDialog.openError(shell, Strings.get("appTitle"),
						"ќшибка создании группы.");
			}

	}

}