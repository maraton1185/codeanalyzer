package ebook.module.booksList.handlers;

import org.eclipse.e4.core.di.annotations.Execute;

import ebook.core.App;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_TREE_DATA;

public class UpdateList {
	@Execute
	public void execute() {
		App.br.post(Events.EVENT_UPDATE_BOOK_LIST,
				new EVENT_UPDATE_TREE_DATA(null, null));
	}

}