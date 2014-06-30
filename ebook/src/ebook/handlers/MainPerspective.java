package ebook.handlers;

import org.eclipse.e4.core.di.annotations.Execute;

import ebook.core.App;
import ebook.core.App.ListParts;
import ebook.core.App.Perspectives;

public class MainPerspective {

	@Execute
	public void execute() {

		App.showPerspective(Perspectives.main, ListParts.current);

	}

}