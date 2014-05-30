package ebook.module.books.handlers;

import javax.inject.Named;

import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.module.books.BookConnection;
import ebook.module.books.tree.SectionInfo;
import ebook.utils.Events;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class AddPicture {
	@Execute
	public void execute(
			Shell shell,
			BookConnection book,
			@Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {

		IPath p = Utils.browseFile(book.getPath(), shell,
				Strings.get("appTitle"), "*.bmp; *.png");
		if (p == null)
			return;

		book.srv().add_image(section, p);
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {

		return section != null && !section.isGroup();
	}
}