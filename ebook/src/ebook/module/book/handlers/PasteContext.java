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
import ebook.module.conf.tree.ContextInfo;
import ebook.utils.Strings;

public class PasteContext {

	@Execute
	public void execute(final @Active BookConnection con,
			@Active final SectionInfo section, @Active final ContextInfo item,
			final Shell shell) {

		final File zipFile = App.contextClip.getZip();

		BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
			@Override
			public void run() {
				try {

					App.contextClip.doPaste();
					con.ctxsrv(section).upload(zipFile.getAbsolutePath(), item);

				} catch (Exception e) {
					e.printStackTrace();
					MessageDialog.openError(shell, Strings.get("appTitle"),
							e.getMessage());
				}
			}
		});

	}

	@CanExecute
	public boolean canExecute(@Optional @Active SectionInfo section,
			@Optional @Active ContextInfo context) {
		return section != null && context != null && !App.contextClip.isEmpty();
	}

}