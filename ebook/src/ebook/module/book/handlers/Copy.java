package ebook.module.book.handlers;

import java.io.File;
import java.io.IOException;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfoSelection;
import ebook.utils.Strings;

public class Copy {
	@Execute
	public void execute(final @Active BookConnection book,
			@Active final SectionInfoSelection selection, final Shell shell) {

		try {
			final File zipFile = File.createTempFile("copy", ".zip");

			BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
				@Override
				public void run() {
					try {
						book.srv().download(null, selection,
								zipFile.getAbsolutePath(), true);

						App.bookClip.setCopy(zipFile, book, selection);

					} catch (Exception e) {
						e.printStackTrace();
						MessageDialog.openError(shell,
								Strings.title("appTitle"), e.getMessage());
					}
				}
			});

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	@CanExecute
	public boolean canExecute(@Optional @Active SectionInfoSelection selection) {
		return selection != null && !selection.isEmpty();
	}

}