package codeanalyzer.module.books.handlers.list;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.core.AppManager;
import codeanalyzer.core.Events;
import codeanalyzer.module.books.list.CurrentBookInfo;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class InfoImage {
	@Execute
	public void execute(CurrentBookInfo book, Shell shell) {
		IPath p = Utils.browseFile(book.getPath(), shell,
				Strings.get("appTitle"), "*.bmp; *.png");
		if (p == null)
			return;

		String ext = p.getFileExtension();
		try {
			FileUtils.copyFile(p.toFile(),
					new File(book.getPath().append(book.getName())
							.addFileExtension(ext).toString()));

			AppManager.br.post(Events.EVENT_UPDATE_BOOK_INFO, null);
			AppManager.br.post(Events.EVENT_UPDATE_BOOK_LIST, null);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@CanExecute
	public boolean canExecute(@Optional CurrentBookInfo book) {
		return book != null;
	}

}