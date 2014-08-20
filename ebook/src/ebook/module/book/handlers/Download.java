/**
 * 
 */
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
import ebook.module.book.tree.SectionInfoSelection;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class Download {

	@Execute
	public void execute(final @Active BookConnection book,
			@Active final SectionInfoSelection selection, final Shell shell) {

		final IPath p = Utils.browseDirectory(book.getFullPath(), shell);
		if (p == null)
			return;

		BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
			@Override
			public void run() {
				try {
					String name = book.srv()
							.download(p, selection, null, false);
					MessageDialog.openInformation(shell,
							Strings.title("appTitle"),
							Strings.msg("SaveToFile.sucsess") + " (" + name
									+ ")");
				} catch (Exception e) {
					e.printStackTrace();
					MessageDialog.openError(shell, Strings.title("appTitle"),
							e.getMessage());
				}
			}
		});

	}

	@CanExecute
	public boolean canExecute(@Optional @Active SectionInfoSelection selection) {
		return selection != null && !selection.isEmpty();
	}

}
