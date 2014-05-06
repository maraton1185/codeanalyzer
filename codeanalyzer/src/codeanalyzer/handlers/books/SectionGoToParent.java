package codeanalyzer.handlers.books;

import javax.inject.Named;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.core.AppManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_VIEW_DATA;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class SectionGoToParent {
	@Execute
	public void execute(
			BookInfo book,
			@Active MWindow window,
			@Active @Named(Const.CONTEXT_ACTIVE_VIEW_SECTION) BookSection section,
			ECommandService cs, EHandlerService hs) {

		BookSection selected = book.sections().getParent(section);

		if (selected == null)
			return;

		window.getContext().set(BookSection.class, selected);
		Utils.executeHandler(hs, cs, Strings.get("command.id.ShowSection"));
		// window.getContext().set(BookSection.class, current_section);
		AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW,
				new EVENT_UPDATE_VIEW_DATA(book, null, selected));
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Active @Named(Const.CONTEXT_ACTIVE_VIEW_SECTION) BookSection section) {
		return section != null && section.parent != 0;
	}

}