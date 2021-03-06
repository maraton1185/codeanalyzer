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
	public void execute(final @Active BookConnection book,
			@Active final SectionInfo section, final Shell shell) {

		// if (!pico.get(IAuthorize.class).checkSectionsCount(shell, book))
		// return;

		final File zipFile = App.bookClip.getZip();

		BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
			@Override
			public void run() {
				try {

					App.bookClip.doPaste();
					book.srv().upload(zipFile.getAbsolutePath(), section, true,
							false);

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
		return section != null && !App.bookClip.isEmpty();
	}

}