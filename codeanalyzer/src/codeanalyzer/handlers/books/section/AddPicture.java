package codeanalyzer.handlers.books.section;

import javax.inject.Named;

import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.books.book.BookInfo;
import codeanalyzer.books.section.SectionInfo;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class AddPicture {
	@Execute
	public void execute(
			Shell shell,
			BookInfo book,
			@Active @Named(Const.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {

		IPath p = Utils.browseFile(book.getPath(), shell,
				Strings.get("appTitle"), "*.bmp; *.png");
		if (p == null)
			return;

		book.sections().add_image(section, p);
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Active @Named(Const.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {

		return section != null && section.block;
	}
}