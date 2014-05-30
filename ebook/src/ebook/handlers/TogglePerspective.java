package ebook.handlers;

import org.eclipse.e4.core.di.annotations.Execute;

import ebook.core.App;

public class TogglePerspective {
	@Execute
	public void execute() {

		App.togglePerspective();

	}

}