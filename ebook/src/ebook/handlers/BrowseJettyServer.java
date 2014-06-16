package ebook.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.program.Program;

import ebook.core.App;
import ebook.utils.Events;

public class BrowseJettyServer {
	@Execute
	public void execute() {

		switch (App.getJetty().status()) {
		case started:
			Program.launch(App.getJetty().info());
			break;
		default:
			App.getJetty().setManual();
			App.br.post(Events.EVENT_START_JETTY, null);
			break;
		// case error:
		// break;
		}

	}

}