package ebook.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.program.Program;

import ebook.core.App;
import ebook.utils.Events;

public class BrowseJettyServer {
	@Execute
	public void execute() {
		if (App.getJetty().isStarted())
			Program.launch(App.getJetty().host());
		else {
			App.getJetty().setManual();
			App.br.post(Events.EVENT_START_JETTY, null);
		}

	}

}