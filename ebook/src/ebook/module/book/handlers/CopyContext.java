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
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.tree.SectionInfoOptions;
import ebook.module.conf.tree.ContextInfoSelection;
import ebook.utils.Strings;

public class CopyContext {

	@Execute
	public void execute(final @Active BookConnection con,
			@Active final SectionInfo section,
			@Active final ContextInfoSelection selection, final Shell shell) {

		final SectionInfoOptions opt = section.getOptions();
		if (!opt.hasContext())
			return;

		try {
			final File zipFile = File.createTempFile("copy", ".zip");

			BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
				@Override
				public void run() {
					try {
						con.ctxsrv(section).download(null, selection,
								zipFile.getAbsolutePath(), true);

						App.contextClip.setCopy(zipFile, con, selection);
						App.contextClip.setConnectionName(opt.getContextName());

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
	public boolean canExecute(@Optional @Active ContextInfoSelection selection,
			@Optional @Active SectionInfo section) {
		return section != null && selection != null && !selection.isEmpty();

	}

}