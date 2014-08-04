package ebook.module.conf.handlers;

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
import ebook.module.conf.ConfConnection;
import ebook.module.conf.tree.ContextInfo;
import ebook.utils.Strings;

public class Copy {
	@Execute
	public void execute(final @Active ConfConnection con,
			@Active final ContextInfo item, final Shell shell) {

		try {
			final File zipFile = File.createTempFile("copycontext", ".zip");

			BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
				@Override
				public void run() {
					try {
						con.srv().download(null, item,
								zipFile.getAbsolutePath());

						App.contextClip.setCopy(zipFile, con, item);
						// MessageDialog.openInformation(shell,
						// Strings.get("appTitle"),
						// Strings.get("message.SaveToFile.sucsess"));
					} catch (Exception e) {
						e.printStackTrace();
						MessageDialog.openError(shell, Strings.get("appTitle"),
								e.getMessage());
					}
				}
			});

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	@CanExecute
	public boolean canExecute(@Optional @Active ContextInfo iten) {
		return iten != null;
	}

}