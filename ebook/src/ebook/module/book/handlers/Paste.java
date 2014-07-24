package ebook.module.book.handlers;

import java.io.File;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.utils.Strings;

public class Paste {

	@Execute
	public void execute(final BookConnection book,
			@Active final SectionInfo section, final Shell shell) {

		final File zipFile = App.clip.getZip();

		BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
			@Override
			public void run() {
				try {

					App.clip.doPaste();
					book.srv().upload(zipFile.getAbsolutePath(), section);

				} catch (Exception e) {
					e.printStackTrace();
					MessageDialog.openError(shell, Strings.get("appTitle"),
							e.getMessage());
				}
			}
		});

	}

	@CanExecute
	public boolean canExecute(@Optional @Active SectionInfo section) {
		return section != null && !App.clip.isEmpty();
	}

}