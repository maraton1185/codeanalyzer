package ebook.module.confList.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.module.conf.ConfConnection;
import ebook.utils.Events;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class serviceShow {

	@Inject
	@Optional
	public void EVENT_SHOW_BOOK(@UIEventTopic(Events.EVENT_SHOW_CONF) Object o,
			final EHandlerService hs, final ECommandService cs) {

		Utils.executeHandler(hs, cs, Strings.get("command.id.ShowConf"));
	}

	@CanExecute
	public boolean canExecute(@Optional ConfConnection conf) {
		return conf != null;
	}

	@Execute
	public void execute(EPartService partService, EModelService model,
			final @Active ConfConnection conf, IEclipseContext ctx,
			EHandlerService hs, ECommandService cs, Shell shell) {
		MessageDialog.openInformation(shell, Strings.get("appTitle"),
				"открываем окно конфигурации");
		conf.closeConnection();
	}
}