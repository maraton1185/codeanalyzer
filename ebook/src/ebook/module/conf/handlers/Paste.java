package ebook.module.conf.handlers;

import java.io.File;

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
import ebook.module.conf.tree.ContextInfoSelection;
import ebook.utils.Strings;

public class Paste {

	@Execute
	public void execute(final @Active ConfConnection con,
			@Active final ContextInfo item, final Shell shell) {

		final File zipFile = App.contextClip.getZip();

		BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
			@Override
			public void run() {
				try {

					if (App.contextClip.isCut()) {

						ContextInfoSelection sel = new ContextInfoSelection();
						sel.add(item);
						((ConfConnection) App.contextClip.getConnection())
								.srv().delete(sel);
					}
					App.contextClip.doPaste();
					con.srv().upload(zipFile.getAbsolutePath(), item);

				} catch (Exception e) {
					e.printStackTrace();
					MessageDialog.openError(shell, Strings.get("appTitle"),
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