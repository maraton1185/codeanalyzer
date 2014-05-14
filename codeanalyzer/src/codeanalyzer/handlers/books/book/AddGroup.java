package codeanalyzer.handlers.books.book;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.cf.interfaces.ICf;
import codeanalyzer.core.model.BookInfo;
import codeanalyzer.db.interfaces.IDbManager;

public class AddGroup {
	@Execute
	public void execute(@Optional ICf db, Shell shell, IDbManager bm,
			@Optional BookInfo book) {
		InputDialog dlg = new InputDialog(shell,
				codeanalyzer.utils.Strings.get("appTitle"),
				"¬ведите название группы:", "", null);
		if (dlg.open() == Window.OK)
			bm.addBooksGroup(dlg.getValue(), book, false);
	}

}