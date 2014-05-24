package codeanalyzer.module.books.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;

import codeanalyzer.core.App;
import codeanalyzer.module.books.BookConnection;
import codeanalyzer.module.books.section.SectionInfo;
import codeanalyzer.utils.Events;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;
import codeanalyzer.utils.Events.EVENT_UPDATE_VIEW_DATA;

public class GoToParent {
	@Execute
	public void execute(
			BookConnection book,
			@Active MWindow window,
			@Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section,
			ECommandService cs, EHandlerService hs) {

		SectionInfo selected = (SectionInfo) book.srv()
				.get(section.parent);

		if (selected == null)
			return;

		window.getContext().set(SectionInfo.class, selected);
		Utils.executeHandler(hs, cs, Strings.get("command.id.ShowSection"));
		// window.getContext().set(BookSection.class, current_section);
		App.br.post(Events.EVENT_UPDATE_CONTENT_VIEW,
				new EVENT_UPDATE_VIEW_DATA(book, null, selected));
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {
		return section != null && section.parent != 0;
	}

}