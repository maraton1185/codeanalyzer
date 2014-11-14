package ebook.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectToolItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.utils.Strings;

public class BrowseJettyServer {
	@Execute
	public void execute(Shell shell) {
		if (App.getJetty().isStarted())
			Program.launch(App.getJetty().host());
		else {
			App.getJetty().setManual();
			BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
				@Override
				public void run() {
					App.getJetty().start();
				}
			});

			if (!App.getJetty().isStarted()) {

				MessageDialog
						.openError(
								shell,
								Strings.title("appTitle"),
								"Ошибка старта web-сервера.\n"
										+ "Возможные решения:\n"
										+ " - установите флажок \"Использовать внешний каталог web-сервера\" в настройках\n"
										+ " - запустите программу от имени администратора"
										+ " - проверьте права на каталог программы");

			} else

				App.sync.asyncExec(new Runnable() {
					@Override
					public void run() {

						MDirectToolItem d_element = (MDirectToolItem) App.model
								.find(Strings.model("ebook.directtoolitem.1"),
										App.app);
						d_element.setLabel(App.getJetty().jettyMessage());
						d_element.setVisible(false);
						d_element.setVisible(true);

					}
				});
			// App.br.post(Events.EVENT_START_JETTY, null);
		}

	}

}