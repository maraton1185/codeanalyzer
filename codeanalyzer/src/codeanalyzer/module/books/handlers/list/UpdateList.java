package codeanalyzer.module.books.handlers.list;

import org.eclipse.e4.core.di.annotations.Execute;

import codeanalyzer.core.AppManager;
import codeanalyzer.core.Events;
import codeanalyzer.core.Events.EVENT_UPDATE_TREE_DATA;

public class UpdateList {
	@Execute
	public void execute() {
		AppManager.br.post(Events.EVENT_UPDATE_BOOK_LIST,
				new EVENT_UPDATE_TREE_DATA(null, null));
	}

}