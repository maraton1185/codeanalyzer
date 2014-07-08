package ebook.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

import ebook.core.App;
import ebook.utils.Events;
import ebook.web.IJetty.JettyStatus;

public class StopJetty {
	@Execute
	public void execute() {
		//
		App.br.post(Events.EVENT_STOP_JETTY, null);

	}

	@CanExecute
	public boolean canExecute() {

		return App.getJetty().status() == JettyStatus.started;
	}
}