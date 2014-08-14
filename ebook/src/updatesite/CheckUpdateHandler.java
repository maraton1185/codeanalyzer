package updatesite;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.utils.Events;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class CheckUpdateHandler {
	@Execute
	public void execute(Shell shell) {

		BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
			@Override
			public void run() {

				if (App.agent == null)
					return;
				IStatus result = P2Util.checkForUpdates(App.agent, null);
				if (result.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
					Utils.popUpInformation(Strings.msg("updateNotFound"));
				} else {
					App.br.post(Events.SHOW_UPDATE_AVAILABLE, null);
				}
				return;
			}
		});

	}
}
