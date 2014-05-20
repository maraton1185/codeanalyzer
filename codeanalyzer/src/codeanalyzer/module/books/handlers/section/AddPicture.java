package codeanalyzer.module.books.handlers.section;

import javax.inject.Named;

import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.core.Events;
import codeanalyzer.module.books.list.CurrentBookInfo;
import codeanalyzer.module.books.section.SectionInfo;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class AddPicture {
	@Execute
	public void execute(
			Shell shell,
			CurrentBookInfo book,
			@Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {

		IPath p = Utils.browseFile(book.getPath(), shell,
				Strings.get("appTitle"), "*.bmp; *.png");
		if (p == null)
			return;

		book.sections().add_image(section, p);
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {

		return section != null && section.block;
	}
}