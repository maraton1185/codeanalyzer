package codeanalyzer.module.booksList.handlers;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.core.App;
import codeanalyzer.module.booksList.IBookListManager;
import codeanalyzer.utils.Events;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class Open {
	@Execute
	public void execute(Shell shell, IBookListManager bm) {
		IPath p = Utils.browseFile(
				new Path(PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY)),
				shell, Strings.get("appTitle"), "*.db");
		if (p == null)
			return;

		bm.openBook(p, shell);

		App.br.post(Events.EVENT_SHOW_BOOK, null);
	}

}