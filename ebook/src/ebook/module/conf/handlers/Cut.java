package ebook.module.conf.handlers;

import java.io.File;
import java.io.IOException;

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
import ebook.module.conf.tree.ContextInfoSelection;
import ebook.module.conf.tree.ListInfo;
import ebook.utils.Events;
import ebook.utils.Strings;

public class Cut {
	@Execute
	public void execute(final @Active ConfConnection con,
			@Active final ContextInfoSelection sel, final Shell shell,
			@Active @Named(Events.CONTEXT_ACTIVE_LIST) final ListInfo list) {

		try {
			final File zipFile = File.createTempFile("cutcontext", ".zip");
			App.contextClip.setCut(zipFile, con, con.srv(list), sel);

			BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
				@Override
				public void run() {
					try {
						con.srv(list).download(null, sel,
								zipFile.getAbsolutePath());
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
	public boolean canExecute(@Optional @Active ContextInfoSelection sel) {
		return sel != null && !sel.isEmpty();
	}

}