package codeanalyzer.handlers.books;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.core.AppManager;
import codeanalyzer.core.interfaces.IBookManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class OpenBookHangler {
	@Execute
	public void execute(Shell shell, IBookManager bm) {
		IPath p = Utils.browseFile(
				new Path(PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY)),
				shell, Strings.get("appTitle"), "*.db");
		if (p == null)
			return;

		bm.openBook(p, shell);

		AppManager.br.post(Const.EVENT_SHOW_BOOK, null);
	}

}