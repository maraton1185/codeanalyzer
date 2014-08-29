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
import ebook.module.book.tree.SectionInfoOptions;
import ebook.utils.Strings;

public class PasteContext {

	@Execute
	public void execute(final @Active BookConnection con,
			@Active final SectionInfo section, final Shell shell) {

		SectionInfoOptions opt = section.getOptions();
		if (opt.hasContext()

				&& !App.contextClip.getConnectionName().equalsIgnoreCase(
						opt.getContextName())) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Контекст не применим к разделу");
			return;

		}
		final File zipFile = App.contextClip.getZip();

		BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
			@Override
			public void run() {
				try {

					con.ctxsrv(section).upload(zipFile.getAbsolutePath(),
							App.contextClip.getConnectionName(), true);

					App.contextClip.doPaste();

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
		return section != null && !App.contextClip.isEmpty();

	}

}