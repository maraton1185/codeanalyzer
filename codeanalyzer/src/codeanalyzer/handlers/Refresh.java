package codeanalyzer.handlers;

import org.eclipse.e4.core.di.annotations.Execute;

import codeanalyzer.core.App;

public class Refresh {
	@Execute
	public void execute() {

		App.perspectiveActions();

	}

}