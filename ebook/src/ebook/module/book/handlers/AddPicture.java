package ebook.module.book.handlers;

import javax.inject.Named;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionImage;
import ebook.module.book.tree.SectionInfo;
import ebook.utils.Events;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class AddPicture {
	@Execute
	public void execute(
			Shell shell,
			@Active BookConnection book,
			@Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {

		IPath p = Utils.browseFile(
				new Path(PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_IMAGE_DIRECTORY)),
				shell, Strings.get("appTitle"), SectionImage.getFilters());
		if (p == null)
			return;

		book.srv().add_image(section, p, null);
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {

		return section != null && !section.isGroup();
	}
}