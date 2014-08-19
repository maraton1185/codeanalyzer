package ebook.module.conf.handlers;

import java.io.File;

import javax.inject.Named;

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
import ebook.module.conf.tree.ListInfo;
import ebook.utils.Events;
import ebook.utils.Strings;

public class Paste {

	@Execute
	public void execute(final @Active ConfConnection con,
			@Active final ContextInfo item, final Shell shell,
			@Active @Named(Events.CONTEXT_ACTIVE_LIST) final ListInfo list) {

		final File zipFile = App.contextClip.getZip();

		BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
			@Override
			public void run() {
				try {

					App.contextClip.doPaste();
					con.srv(list).upload(zipFile.getAbsolutePath(), item, true,
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
	public boolean canExecute(@Optional @Active ContextInfo section) {
		return section != null && !App.contextClip.isEmpty();
	}

}