package codeanalyzer.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.program.Program;

import codeanalyzer.core.App;

public class BrowseJettyServer {
	@Execute
	public void execute() {
		Program.launch(App.getJetty().info());
	}

}