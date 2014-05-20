package codeanalyzer.module.books.handlers.list;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.core.AppManager;
import codeanalyzer.core.Events;
import codeanalyzer.module.books.interfaces.IBookManager;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class Open {
	@Execute
	public void execute(Shell shell, IBookManager bm) {
		IPath p = Utils.browseFile(
				new Path(PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY)),
				shell, Strings.get("appTitle"), "*.db");
		if (p == null)
			return;

		bm.openBook(p, shell);

		AppManager.br.post(Events.EVENT_SHOW_BOOK, null);
	}

}