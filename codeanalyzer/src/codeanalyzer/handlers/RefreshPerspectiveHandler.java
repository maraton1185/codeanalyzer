package codeanalyzer.handlers;

import org.eclipse.e4.core.di.annotations.Execute;

import codeanalyzer.core.AppManager;

public class RefreshPerspectiveHandler {
	@Execute
	public void execute() {

		AppManager.perspectiveActions();

	}

}