package codeanalyzer.handlers.main;

import org.eclipse.e4.core.di.annotations.Execute;

import codeanalyzer.core.AppManager;

public class Refresh {
	@Execute
	public void execute() {

		AppManager.perspectiveActions();

	}

}