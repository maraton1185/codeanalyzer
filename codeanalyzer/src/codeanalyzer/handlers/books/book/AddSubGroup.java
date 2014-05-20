package codeanalyzer.handlers.books.book;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.books.book.BookInfo;
import codeanalyzer.books.interfaces.IBookManager;
import codeanalyzer.cf.interfaces.ICf;
import codeanalyzer.utils.Strings;

public class AddSubGroup {
	@Execute
	public void execute(@Optional ICf db, Shell shell, IBookManager bm,
			@Optional BookInfo book) {
		InputDialog dlg = new InputDialog(shell,
				codeanalyzer.utils.Strings.get("appTitle"),
				"������� �������� ������:", "", null);
		if (dlg.open() == Window.OK)
			try {

				BookInfo data = new BookInfo();
				data.title = dlg.getValue();
				data.isGroup = true;
				data.path = "";
				bm.addGroup(data, book, true);
				// ((ITreeService) bm).add(data, book, true);

				// bm.addBooksGroup(dlg.getValue(), book, true);
			} catch (InvocationTargetException e) {
				MessageDialog.openError(shell, Strings.get("appTitle"),
						"������ �������� ������.");
			}

	}
}