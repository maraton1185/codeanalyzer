package codeanalyzer.module.booksList.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.module.books.BookConnection;

public class InfoImage {
	@Execute
	public void execute(BookConnection book, Shell shell) {
		// IPath p = Utils.browseFile(book.getPath(), shell,
		// Strings.get("appTitle"), "*.bmp; *.png");
		// if (p == null)
		// return;
		//
		// String ext = p.getFileExtension();
		// try {
		// FileUtils.copyFile(p.toFile(),
		// new File(book.getPath().append(book.getName())
		// .addFileExtension(ext).toString()));
		//
		// // AppManager.br.post(Events.EVENT_UPDATE_BOOK_INFO, null);
		// // AppManager.br.post(Events.EVENT_UPDATE_BOOK_LIST, null);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}

	// @CanExecute
	// public boolean canExecute(@Optional WindowBookInfo book) {
	// return book != null;
	// }

}