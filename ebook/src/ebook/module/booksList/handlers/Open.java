package ebook.module.booksList.handlers;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import ebook.module.booksList.IBookListManager;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class Open {
	@Execute
	public void execute(Shell shell, IBookListManager blm) {
		IPath p = Utils.browseFile(
				new Path(PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY)),
				shell, Strings.get("appTitle"), "*.db");
		if (p == null)
			return;

		blm.openBook(p, shell);

		// App.br.post(Events.EVENT_SHOW_BOOK, null);
	}

}