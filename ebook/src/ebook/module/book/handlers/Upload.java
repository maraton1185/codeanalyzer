package ebook.module.book.handlers;

import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Shell;

import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class Upload {

	@Execute
	public void execute(final @Active BookConnection book,
			@Active final SectionInfo section, final Shell shell) {

		final IPath p = Utils.browseFile(book.getFullPath(), shell,
				Strings.title("appTitle"), "*.zip");
		if (p == null)
			return;

		if (!p.toFile().exists())
			return;

		BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
			@Override
			public void run() {
				try {
					book.srv().upload(p.toString(), section);
				} catch (Exception e) {
					e.printStackTrace();
					MessageDialog.openError(shell, Strings.title("appTitle"),
							e.getMessage());
				}
			}
		});

	}

	@CanExecute
	public boolean canExecute(@Optional @Active SectionInfo section) {
		return section != null;
	}

}