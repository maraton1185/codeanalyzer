package codeanalyzer.handlers.books.book;

import org.eclipse.e4.core.di.annotations.Execute;

import codeanalyzer.core.AppManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_BOOK_LIST_DATA;

public class UpdateList {
	@Execute
	public void execute() {
		AppManager.br.post(Const.EVENT_UPDATE_BOOK_LIST,
				new EVENT_UPDATE_BOOK_LIST_DATA(null, null));
	}

}