package ebook.module.book.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.program.Program;

import ebook.core.App;
import ebook.module.book.BookConnection;

public class OpenInBrowser {
	@Execute
	public void execute(@Active BookConnection book) {
		if (App.getJetty().isStarted())
			Program.launch(App.getJetty().host()
					+ App.getJetty().book(book.getTreeItem().getId()));

	}
}