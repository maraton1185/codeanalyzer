package codeanalyzer.handlers.books;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.book.BookInfo;
import codeanalyzer.core.AppManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class BookInfoImageHandler {
	@Execute
	public void execute(BookInfo book, Shell shell) {
		IPath p = Utils.browseFile(book.getPath(), shell,
				Strings.get("appTitle"), "*.bmp; *.png");
		if (p == null)
			return;

		String ext = p.getFileExtension();
		try {
			FileUtils.copyFile(p.toFile(),
					new File(book.getPath().append(book.getName())
							.addFileExtension(ext).toString()));

			AppManager.br.post(Const.EVENT_UPDATE_BOOK_INFO, null);
			AppManager.br.post(Const.EVENT_UPDATE_BOOK_LIST, null);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@CanExecute
	public boolean canExecute(@Optional BookInfo book) {
		return book != null;
	}

}